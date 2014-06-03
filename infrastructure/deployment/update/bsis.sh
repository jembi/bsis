#!/bin/bash

SOURCEDIR="/git/bsis"

cd $SOURCEDIR;
git pull && puppet apply infrastructure/deployment/update/bsis.pp