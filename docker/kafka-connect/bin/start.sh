#!/bin/bash
set -e

sh init_connectors.sh > connectors.log &
sh /etc/confluent/docker/run
