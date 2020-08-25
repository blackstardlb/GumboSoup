#!/bin/bash
./autogen.sh
mkdir output
output_path=$(readlink -f output)
./configure
make
make install exec_prefix=$output_path prefix=$output_path
