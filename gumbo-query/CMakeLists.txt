project(gumbo_query CXX)
cmake_minimum_required(VERSION 3.2 FATAL_ERROR)

include(cmake/PreventInSourceBuilds.cmake)

# Debug build.
set(CMAKE_VERBOSE_MAKEFILE ON)
set(CMAKE_BUILD_TYPE Debug)

# Set our output directories.
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/lib)
set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY ${CMAKE_LIBRARY_OUTPUT_DIRECTORY})

# Create library directory if it does not exist.
file(MAKE_DIRECTORY ${CMAKE_LIBRARY_OUTPUT_DIRECTORY})

# Add the cmake modules directory to our module path.
set(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} ${PROJECT_SOURCE_DIR}/cmake)
find_package(Gumbo REQUIRED)

# Set our library source directory.
set(gumbo_query_SOURCE_DIRECTORY ${PROJECT_SOURCE_DIR}/src)

# Add source subdirectories.
add_subdirectory(src)
