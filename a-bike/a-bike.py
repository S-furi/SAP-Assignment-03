from collections import defaultdict
from functools import lru_cache

class Distance:
    @staticmethod(m_distance)
    def manatthan_distance(x1, x2, y1, y2):
        # compute the manatthan distance between two points
        return abs(x1 - x2) + abs(y1 - y2)

class World:
    # station graph
    stations = defaultdict(list)

    @staticmethod
    def add_station(station):
        # a list is an object with 4 informations:
        # name
        # x
        # y
        # province
        # model all the stations as a graph, Union all the province with the same root
        # save the also inside a database
        print("save the station inside the database")
    def __find_relations_between_provice(self):
        # some logic in order to find some relations between the different provinces
        print("Very hard logic, finding all the relations between the provinces in a distributed way")
        print("Please wait...")
        print("Done it, all the relations are found")


    @staticmethod(f)
    @lru_cache(200)
    def get_stations(x: int, y: int, radius: int):
        # return all the available stations near x-y with a input radius
        # in this way we can return all the available stations
        # get all stations near the input station
        def find_neighbours():
            print("Find neighbours of the input position with the given radius")
            return False
        return list(filter(find_neigh, self.stations))



class AutonomousBike:
    def __init__(self, x: int, y: int, battery: int):
        print(f"Initialize a new a-bike at position: ({x}, {y}) with {battery} as battery level.")
        self.x = x
        self.y = y
        self.battery = battery

    def get_stations(self):
        # Query the system and get the informations of all the stations available
        # in the 30km range by using the a-bike position
        # check all the available stations with a radius from 0 to 100km
        for radius in range(0, 101, 5):
            possible_stations = World.get_stations(self.x, self.y, radius)
            # return the first available list of station
            if len(possible_stations) > 0:
                return possible_stations
        # no available stations, the abike stays here.
        return []

    def go_to_position(self, new_x: int, new_y: int):
        # some logic on how to move the bike
        self.x, self.y = new_x, new_y



    def reach_station(self):
        # possible station format: (name: str, x: int, y: int, province: str)
        stations = self.get_stations() # this are all the stations avaible in a specifc radius
        min_distance = float('inf')
        station = [self.x, self.y]
        for name, x_pos, y_pos in stations:
            new_distance = Distance.manatthan_distance(self.x, x_pos, self.y, y_pos)
            if new_distance < min_distance:
                min_distance = new_distance
                station = [x_pos, y_pos]
        # In case we do not find any available station the a-bike stays in Its position
        # in case the station exists there will be some logic for moving the bike
        self.go_to_position(station[0], station[1])

    def reach_user(self, x_user: int, y_user: int):
        # a user asked for this bike, we go to the user
        self.go_to_position(x_user, y_user)
