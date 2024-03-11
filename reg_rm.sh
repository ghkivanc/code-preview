#!/bin/bash

find $1 | egrep $2 | xargs -d"\n" rm -r 