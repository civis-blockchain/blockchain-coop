#!/usr/bin/env node


/*
 * Build a context object from args
 */

var args = ["port", "user", "endorsers", "channel", "ccid"];

function usage() {
	var msg = "Usage:\n\t" + process.argv[1].split("/").pop();
	args.map((x) => {msg += " <" + x + ">";});
	msg += "\nendorders list format: peer0:org0,peer1:org0,peerx:orgx...";
	msg += "\nfirst endorder peer:org pair is used for queries.";
	console.log(msg)
}

if (process.argv.length < args.length + 2) {
	usage();
	process.exit(1);
}

var index = 2;

var context = {
};

args.map((x) => {context[x] = process.argv[index++];});
// Build context endorsers for invoke commands
var peersOrgs = context.endorsers.split(',');
context.endorsers = [];
peersOrgs.map(function(peerOrgStr)
{
	var peerOrgArray = peerOrgStr.split(':');
	context.endorsers.push(
	{
		'peer': peerOrgArray[0],
		'org': peerOrgArray[1]
	});
});
// Build context peer/org for query commands
context.peer = context.endorsers[0].peer;
context.org = context.endorsers[0].org;


/*
 * BlockchainCoop command callbacks
 */

var errHandler = function(err) {
	console.log(err.stack ? err.stack : err);
	var result = err.toString ? err.toString() : "Error: blockchain call failure";
	httpResHandler(this.res, result);
};

var successHandler = function(msg) {
	httpResHandler(this.res, msg);
};

/*
 * Create a BlockchainCoop instance
 */

var BlockchainCoop = require("./blockchain-coop.js").BlockchainCoop;


/*
 * HTTP requests handlers
 */
var httpResHandler = function(res, result) {
	res.writeHead(200, {
		'Content-Type': 'application/json',
		'Access-Control-Allow-Methods': 'POST, GET, OPTIONS',
		'Access-Control-Allow-Headers': 'Content-Type',
		'Access-Control-Allow-Origin': '*'}
	);
	var str = null;
	try {
		str = JSON.stringify(result);
	}
	catch (error) {
		str = result;
	}
	res.write(str);
	res.end();
};

var httpReqHandler = function(req, res) {
	try {
		if (req.method === 'POST') {
			var body = '';
			req.on('data', (data) => {
				body += data;
			});
			req.on('end', () => {
				var params = parsePostBody(req, body);
				perform(res, params);
			});
		} else if (req.method === 'GET') {
			var params = url.parse(req.url, true).query;
			perform(res, params);
		} else if (req.method === 'OPTIONS') {
			httpResHandler(res, "")
		}
	} catch(err) {
		errHandler(err);
	}
};

var parsePostBody = function(req, data) {
	var contentType = req.headers["content-type"] || "";
	if(contentType.startsWith('application/x-www-form-urlencoded')) {
		return querystring.parse(data);
	} else if(contentType.startsWith('application/json')) {
		return JSON.parse(data);
	} else {
		return {};
	}
};

var perform = function(res, reqArgs) {
	var command = reqArgs.cmd;
	context.fcn = reqArgs.fcn;
	context.args = reqArgs.args;
	var bcc = new BlockchainCoop(context);
	var cbctx = bcc.perform(command, context, successHandler, errHandler);
	cbctx.res = res;
};



/*
 * Create HTTP server
 */

const http = require('http');
const url = require('url');
const querystring = require('querystring');


var srvr = http.createServer(httpReqHandler);
http.createServer(httpReqHandler);
console.log("Starting REST server on port " + context.port);

srvr.listen(context.port);

