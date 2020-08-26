#!/bin/bash
./autogen.sh
mkdir output
output_path=$(pwd)/output
./configure
make
make install exec_prefix=$output_path prefix=$output_path
