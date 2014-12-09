/*
 * Bus stops - a preliminary study for development of Robocar City Emulator
 * Copyright (C) 2014 Norbert Bátfai, batfai.norbert@inf.unideb.hu
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
*/

#include <iostream>
#include <cstddef>
#include <osmium/io/any_input.hpp>
#include <osmium/handler.hpp>
#include <osmium/visitor.hpp>
#include <osmium/osm/node.hpp>
#include <osmium/osm/way.hpp>
#include <osmium/osm/relation.hpp>
#include <osmium/index/map/sparse_table.hpp>
#include <osmium/handler/node_locations_for_ways.hpp>
#include <osmium/geom/haversine.hpp>
#include <osmium/geom/coordinates.hpp>

class BusHandler : public osmium::handler::Handler {
public:

     int stops = 0;
     int utak = 0;
     double sum_bus_length;

     osmium::index::map::SparseTable<osmium::unsigned_object_id_type, osmium::Location> locations;
     std::map<osmium::unsigned_object_id_type, std::vector<osmium::unsigned_object_id_type>> way_node_map;

     void way(osmium::Way& w) {
          ++utak;
          osmium::WayNodeList& wnl = w.nodes();
          std::vector<osmium::unsigned_object_id_type> nds;

          for( osmium::NodeRef& n : wnl )
          {
               nds.emplace_back(n.ref());
          }

          way_node_map[w.id()] = nds;
          nds.clear();
     }

     void relation ( osmium::Relation& rel ) {
          const char* bus = rel.tags() ["route"];
          const char* nev;
          if ( bus && !strcmp ( bus, "bus" ) ) {
               if( rel.tags()["name"])
               {
                    nev = rel.tags()["name"];
               }
               else
               {
                    nev = rel.tags()["ref"];
               }
               ++stops;
               //TODO asdasd


               int i {1};
               osmium::RelationMemberList& rml = rel.members();
               int szam=0;
               for ( osmium::RelationMember& rm : rml ) {
                    i = 1;
                    if (rm.type() == osmium::item_type::way ) {

                         for( auto it : way_node_map[rm.ref()] )
                         {
                              try {
                                   osmium::Location loc = locations.get ( it );

                                   if ( i++>1 ) {
                                        osmium::geom::Coordinates coords {loc};
                                        szam++;
                                        printf("%.8f %.8f\n",double(coords.y),double(coords.x) );

                                   }
                              } catch ( std::exception& e ) {
                                   std::cout << "No such node on the map. "<< e.what() << std::endl;
                              }
                         }
                    std::cout << "way\n";
                    }
               }
               //std::cout << "way\n";
               std::cout << nev << " busz\n";
          }

     }
};

int main ( int argc, char* argv[] )
{
     if ( argc == 2 ) {

          osmium::io::File infile ( argv[1] );
          osmium::io::Reader reader ( infile, osmium::osm_entity_bits::all );

          BusHandler bus_handler;
          osmium::handler::NodeLocationsForWays<osmium::index::map::SparseTable<osmium::unsigned_object_id_type, osmium::Location>>
                    node_locations ( bus_handler.locations );

          osmium::apply ( reader, node_locations, bus_handler );
          reader.close();

          google::protobuf::ShutdownProtobufLibrary();

     } else {

          std::cout << "Usage: " << argv[0] << "city.osm" << std::endl;
          std::exit ( 1 );

     }
}
