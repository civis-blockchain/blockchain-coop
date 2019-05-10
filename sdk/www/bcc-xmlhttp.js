/*
 * example XMLHttpRequest client for the bcc-rest server
 */

function bccHostCmd(uri, cmd, fcn, args, onOk, onError) {

	var cbctx = {
		onOk: onOk,
		onError: onError
	};

	var xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange = function() {
		if (this.readyState != 4)
			return;
		if (this.status != 200) {
			if (cbctx.onError)
				cbctx.onError(this.responseText);
		}
		else {
			if (cbctx.onOk)
				cbctx.onOk(this.responseText);
		}
	};

	var query = "cmd=" + encodeURIComponent(cmd) + "&fcn=" + encodeURIComponent(fcn);
	args.map(function(arg) {query += "&args=" + encodeURIComponent(arg);});
	if(cmd === 'invoke') {
		var json = {
			cmd: cmd,
			fcn: fcn,
			args: args
		};
		xmlhttp.open("POST", uri, true);
		xmlhttp.setRequestHeader("Content-Type", "application/json");
		xmlhttp.send(json);
	} else {
		xmlhttp.open("GET", uri + "?" + query, true);
		xmlhttp.send();
	}

	return cbctx;
}
