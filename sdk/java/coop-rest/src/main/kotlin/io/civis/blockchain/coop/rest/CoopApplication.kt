package io.civis.blockchain.coop.rest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackageClasses = [CoopApplication::class] )
class CoopApplication

fun main(args: Array<String>) {
	runApplication<CoopApplication>(*args)
}