Game.factory('$farmer', ['$http', 'State', function($http, State) {
	
	var self = this;
	self.start_scene = 'buy_terrain';

    function swap(farmer) {
        if (farmer && farmer.hasOwnProperty('balans')) {
            State.set('farmer', farmer);
        }
    }

    function setStatus(status) {
        State.set('status', status);
        swap(status.farmer);
    }
    
   

    return {
        load: function() {
            var res = $http.get("/AuthController/farmer");
            res.success(function(data) {
                setStatus(data);
                swap(data.farmer);
                //        State.set("farmer",data);
                //        $day.load(data);
            });
            return res;
        },
        swap: swap,
        setStatus: setStatus
    };

}]);

Game.factory('$day', [
    '$rootScope',
    'State',
    '$http',
    '$weather',
    '$rootScope',
    'Diseases',
    'BoughtItems',
    '$location',
    'audioManager',
    function($rootScope, State, $http, $weather, $rootScope, Diseases, BoughtItems,
        $location, audioManager) {

    	self.start_scene = 'buy_terrain';
    	
        function onFarmer(data) {
        	if (data.balans === undefined) {
        		farmer = data.farmer;
        	} else {
        		farmer = data;
        	}
            if (!$rootScope.farmer && farmer.field) {
                BoughtItems.load();
            }
            if (farmer.balans != null) {
                $rootScope.day = farmer;
                audioManager.notify($rootScope.day.gameDate.weatherType.music_url);
                $rootScope.farmer = farmer;
            }

            $location.path(farmer.currentState || self.start_scene);
        }
        
        State.subscribe('farmer', '$farmer', onFarmer);

        function setHintEvent(status) {
        	if (status.event!==undefined) {
        	State.set('hint-event', status.event);
        	}
        }
        
        return {
            get: function() {
                return State.getByField('farmer');
            },
            load: function(status) {
                State.set('farmer', status.farmer);
            },
            next: function() {
            	$rootScope.overlayDark = true;
                var res = $http.get("/application/nextday");
                res.success(function(data) {
                    State.set('status', data);
                    setHintEvent(data);
                    if (!$rootScope.farmer && data.farmer.field) {
                        BoughtItems.load();
                    }
                    if (data.balans != null) {
                        $rootScope.day = data;
                        $rootScope.farmer = data;
                    }

                    $location.path(data.farmer.currentState || self.start_scene);
                    $weather.load();
                    Diseases.load();
                    $rootScope.overlayDark = false;
                });
            },
            nextWeek: function() {
            	$rootScope.overlayDark = true;
                var res = $http.get("/application/nextweek");
                res.success(function(data) {
                    State.set('status', data);
                    setHintEvent(data);
                    if (!$rootScope.farmer && data.field) {
                        BoughtItems.load();
                    }
                    if (data.balans != null) {
                        $rootScope.day = data;
                        $rootScope.farmer = data;
                    }
                    $location.path(data.farmer.currentState || self.start_scene);
                    $weather.load();
                    Diseases.load();
                    $rootScope.overlayDark = false;
                });
            },
            nextMonth: function() {
            	$rootScope.overlayDark = true;
                var res = $http.get("/application/nextmonth");
                res.success(function(data) {
                    State.set('status', data);
                    setHintEvent(data);
                    if (!$rootScope.farmer && data.field) {
                        BoughtItems.load();
                    }
                    if (data.balans != null) {
                        $rootScope.day = data;
                        $rootScope.farmer = data;
                    }
                    $location.path(data.farmer.currentState || self.start_scene);
                    $weather.load();
                    Diseases.load();
                    $rootScope.overlayDark = false;
                });
            }
        };
    }
]);

Game.factory('$weather', [
    '$rootScope',
    '$http',
    function($rootScope, $http) {
        if (!$rootScope.weather) {
            $rootScope.weather = [];
        }
        return {
            load: function() {
                var res = $http
                    .get("/WeatherController/weatherforecast?fordays=5");
                res.success(function(data) {
                    $rootScope.weather = data;
                });
            }
        };
    }
]);
