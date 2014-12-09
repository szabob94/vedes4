#
# Bus stops - a preliminary study for development of Robocar City Emulator
# Copyright (C) 2014 Norbert Bátfai, batfai.norbert@inf.unideb.hu
#
#  This program is free software: you can redistribute it and/or modify
#  it under the terms of the GNU General Public License as published by
#  the Free Software Foundation, either version 3 of the License, or
#  (at your option) any later version.
#
#  This program is distributed in the hope that it will be useful,
#  but WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  GNU General Public License for more details.
#
#  You should have received a copy of the GNU General Public License
#  along with this program.  If not, see <http://www.gnu.org/licenses/>.
#

AC_INIT([bus-stops], [0.0.1], [nbatfai@gmail.com])
AC_CONFIG_FILES([Makefile])
AM_INIT_AUTOMAKE
AC_CHECK_LIB(pthread, pthread_create)
PKG_CHECK_MODULES([protobuf], [protobuf])
AC_CONFIG_MACRO_DIR([m4])
AC_PROG_CXX
AC_PROG_LIBTOOL
AC_LANG([C++])
AC_LANG_CPLUSPLUS
CXXFLAGS='-Wall -std=c++11 -O3'
AX_CXX_COMPILE_STDCXX_11()

CXXFLAGS="$AM_CXXFLAGS $CXXFLAGS"

AC_CHECK_HEADER([osmium/osm/relation.hpp])
AC_CHECK_HEADER([osmium/io/any_input.hpp],,
[AC_MSG_ERROR([please install the libosmium header-only library])])

LIBS="-lbz2 $LIBS"

AC_ARG_WITH([bz2],, 
[AC_MSG_ERROR([please install the bz2 library])])

LIBS="-lz $LIBS"

AC_ARG_WITH([z],, 
[AC_MSG_ERROR([please install the z library])])

LIBS="-losmpbf $LIBS"

AC_ARG_WITH([osmpbf],, 
  [AC_MSG_ERROR([please install the osmpbf library])])

LIBS="-lprotobuf-lite $LIBS"

LIBS="-lexpat $LIBS"

AC_ARG_WITH([expat],, 
[AC_MSG_ERROR([please install the expat library])])

AC_SUBST([LIBS])
AC_SUBST([AM_CXXFLAGS])
AC_SUBST([AM_LDFLAGS])

AC_CONFIG_FILES([
 src/Makefile 
 ])
AC_OUTPUT