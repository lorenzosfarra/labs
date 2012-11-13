#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <dlfcn.h>
#include "timelab.h"

time_t time(time_t *tp) {
  printf("Custom time()!\n");
  time_t (*real_time)(size_t) = NULL;
  void *lib = NULL;
  //lib = dlopen("/usr/lib/libc.so", RTLD_DEEPBIND);
  if (!real_time) {
    real_time = dlsym(RTLD_NEXT, "time");
  }
  
  time_t p = real_time(NULL);
  printf("current time is: %ld\n\n", p / 3600);
  return NULL;
}

