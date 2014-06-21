Game.factory('$farmer', [ '$rootScope', '$http', '$items', '$location', '$day',
		function($rootScope, $http, $items, $location, $day) {
			function swap(farmer) {
				if (!$rootScope.farmer) {
					$items.load();
				}
				if ($rootScope.farmer === farmer)
					return;
				$rootScope.farmer = farmer;

				$location.path(farmer.currentState || '/buy_tractor');
			}
			return {
				load : function() {
					/*
					 * if ($rootScope.farmer) { return $rootScope.farmer; } else {
					 */
					var res = $http.get("/AuthController/farmer");
					res.success(function(data) {
						swap(data);
						$day.load(data);
					});
					return res;
					// }
				},
				swap : swap
			};

		} ]);

Game.factory('$items', [ '$rootScope', '$http', function($rootScope, $http) {
	if (!$rootScope.items) {
		$rootScope.items = {};
	}
	return {
		load : function() {
			var res = $http.get("/storecontroller/myitems");
			res.success(function(data) {
				$rootScope.items = {};
				$rootScope._items = data || [];
				for (var i = 0; i < data.length; i++) {
					var store = data[i].store;
					$rootScope.items[store] = $rootScope.items[store] || [];
					$rootScope.items[store].push(data[i]);
				}
			});
		},
		add : function(key, val) {
			$rootScope.items[key] = $rootScope.items[key] || [];
			var vals = $rootScope.items[key];
			// todo: check for duplicates
			vals.push(val);
			$rootScope._items.push(val);
		},
		check : function(key) {
			return $rootScope.items[key] && $rootScope.items[key].length > 0;
		},
		get : function(key) {
			return $rootScope.items[key];
		},
		use : function(key, item) {
			$rootScope.items[key].pop();

			function remove(arr) {
				var results = [];
				var removed = false;
				angular.forEach(arr, function(val) {

					if (!removed && val.store == key) {
						if (!item || val.id == item.id) {
							removed = true;
						} else {
							results.push(val);
						}
					} else {
						results.push(val);
					}
				});
				return results;
			}
			$rootScope._items = remove($rootScope._items);
			$rootScope.items[key] = remove($rootScope.items[key]);
		},
		all : function() {
			var results = [];
			angular.forEach($rootScope.items, function(val) {
				angular.forEach(val, function(v) {
					results.push(v);
				});
			});

			console.log(results)
			return results;

		}

	};
} ]);

Game
		.factory(
				'$weather',
				[
						'$rootScope',
						'$http',
						function($rootScope, $http) {
							if (!$rootScope.weather) {
								$rootScope.weather = [];
							}
							return {
								load : function() {
									var res = $http
											.get("/WeatherController/weatherforecast?fordays=5");
									res.success(function(data) {
										$rootScope.weather = data;
									});
								}
							};
						} ]);

Game.factory('$plantation', [
		'$rootScope',
		'$http',
		function($rootScope, $http) {
			return {
				load : function() {
					if (!$rootScope.plantation) {
						var res = $http.get("/AuthController/plantation");
						res.success(function(data) {
							$rootScope.plantation = data;
						});
					}
				},
				save : function(array, fn) {
					var res = $http
							.post("/PlantationController/savePlanting?array="
									+ array);
					res.success(function(data) {
						$rootScope.$emit(fn);
					})
				}
			};
		} ]);

Game.factory('$diseases', [ '$rootScope', '$http', function($rootScope, $http) {
	return {
		load : function() {
			var res = $http.get('/DeseasesExpertSystem/getDeseasePossibility');
			res.success(function(data) {
				$rootScope.diseases = data;
			});
		}
	};
} ]);

Game.factory('$day', [ '$rootScope', '$http', '$weather', '$diseases',
		function($rootScope, $http, $weather, $diseases) {
			return {
				load : function(farmer) {
					$rootScope.day = farmer;

				},
				next : function() {
					var res = $http.get("/application/nextday");
					res.success(function(data) {
						$rootScope.day = data;

						$weather.load();
						$diseases.load();
					});
				}
			};
		} ]);

Game.factory('$irrigate', [
		'$rootScope',
		'$http',
		'$day',
		function($rootScope, $http, $day) {
			return {
				irrigate : function(name, time) {
					var res = $http
							.get("/IrrigationController/irrigation?name="
									+ name + "&time=" + time
									+ "&hasTensiometers=false");
					res.success(function(data) {
						$day.load(data);
						// $weather.load();
						// $diseases.load();
					});
				}
			};
		} ]);
Game.factory('$plowing', [ '$rootScope', '$http', '$day',
		function($rootScope, $http, $day) {
			return {
				plowing : function() {
					var res = $http.get("/LandTreatmanController/plowing");
					res.success(function(data) {
						$day.load(data.farmer);
						if (data.status == false) {
							alert(data.message);
						}
					});
				}
			};
		} ]);
