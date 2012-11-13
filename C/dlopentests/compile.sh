#!/bin/bash

set -x

rm *.so *.o testTime
gcc -c -ldl -fpic timelab.c
gcc -ldl -shared -o libtimelab.so timelab.o
#gcc -ldl -fpic -shared -o timelab.c -o libtimelab.so
gcc testTime.c -o testTime -L . 
