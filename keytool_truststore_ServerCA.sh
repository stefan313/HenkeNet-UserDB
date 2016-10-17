#!/bin/bash

if($1 == "")
	echo "meh, kein cert"
	exit

echo "generating truststore for UserTool with ${1}!"


