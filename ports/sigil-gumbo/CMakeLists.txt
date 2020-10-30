cmake_minimum_required(VERSION 3.8.0)

project(sigil-gumbo)

# Source files
file(GLOB LIB_SRC src/*.c)

set(gumbo_srcs ${LIB_SRC})

include_directories(src)
if (MSVC)
  include_directories(visualc/include)
endif (MSVC)

add_library(sigil-gumbo ${gumbo_srcs})

install(
  TARGETS sigil-gumbo
  RUNTIME DESTINATION bin
  LIBRARY DESTINATION lib
  ARCHIVE DESTINATION lib
)
if(NOT DISABLE_INSTALL_HEADERS)
  install(FILES  src/gumbo.h src/tag_enum.h src/gumbo_edit.h DESTINATION include)
endif()
