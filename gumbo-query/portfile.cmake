include(vcpkg_common_functions)

vcpkg_check_linkage(ONLY_STATIC_LIBRARY)

vcpkg_from_github(
  OUT_SOURCE_PATH SOURCE_PATH
  REPO lazytiger/gumbo-query
  REF 9a269cd
  SHA512  31bf84318f9bd22a8ce58a5be7b482b7dc3c25d852518240b8248c5e61c140858afd4ba64e0baad59ba6a4e55bdd46373f1ce369ac71054844e36f626fcf46bf
  HEAD_REF master
)

file(COPY ${CMAKE_CURRENT_LIST_DIR}/src/CMakeLists.txt DESTINATION ${SOURCE_PATH}/src)
file(COPY ${CMAKE_CURRENT_LIST_DIR}/CMakeLists.txt DESTINATION ${SOURCE_PATH})
file(COPY ${CMAKE_CURRENT_LIST_DIR}/FindGumbo.cmake DESTINATION ${SOURCE_PATH}/cmake)

vcpkg_configure_cmake(
    SOURCE_PATH ${SOURCE_PATH}
    PREFER_NINJA
)

vcpkg_install_cmake()
file(INSTALL ${SOURCE_PATH}/LICENSE DESTINATION ${CURRENT_PACKAGES_DIR}/share/gumbo-query RENAME copyright)
file(REMOVE_RECURSE "${CURRENT_PACKAGES_DIR}/debug/include")
