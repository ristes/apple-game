Game.controller('PlantingStateController', [
    '$scope',
    'Operations',
    '$farmer',
    'Plantation',
    '$weather',
    '$window',
    'Planting',
    'State',
    function($scope, Operations, $farmer, Plantation, $weather, $window,
        Planting, State,ExpertAdvice) {

        $scope.availableSeedlings = 0;

        
        $scope.startNumber = 0;
        
        $scope.gameState = State.gameState();
        

        Planting.availableSeedlings(function(num) {
        	$scope.startNumber = num;
            $scope.availableSeedlings = num;
        });

        $scope.showShop = function() {
            $scope.$root.$emit('operation-store', {
                ico: "/public/images/game/operations/shop.png",
                name: 'store',
                price: 0,
                duration: 0,
                order: 1
            });
        };

        $scope.onResizeFunction = function() {
            $scope.atom = $window.innerWidth * 0.8 / 20;
        };

        // Call to the function when the page is first loaded
        $scope.onResizeFunction();

        angular.element($window).bind('resize', function() {
            $scope.onResizeFunction();
            $scope.$apply();
        });
        $weather.load();
        $farmer.load();
        $scope.actions = Operations['planting'];

        $scope.plantation = State.getByField("plantation");

        if (!$scope.plantation) {
            Plantation.load(function(data) {
                $scope.plantation = data;
                $scope.coords = JSON.parse($scope.plantation.treePositions);
            });
            $scope.coords = [];
        } else {
            $scope.coords = JSON.parse($scope.plantation.treePositions);
        }

        $scope.seedling = $scope.coords.length;
        $scope.totalSeedling = 40;

        var isInCoords = function(p) {
            var arr = $scope.coords;

            for (var i = 0; i < arr.length; i++) {
                var el = arr[i];
                if (el.x === p.x && el.y === p.y) {
                    return true;
                }
            }
            return false;
        }

        $scope.plantingDone = function() {

            function successPlanting(data) {
                if (data.status == true) {
                	debugger;
                	$scope.totalPlantedWhenFinished = $scope.startNumber - $scope.availableSeedlings;
                	 State.set('notification', {
                		 title: 'Congratulations!',
                         message: "You’ve successfully planted: "+$scope.totalPlantedWhenFinished+" apple seddlings. but you have too many lumps left in your field, maybe you should think about doing a second ploughing…"
                       });
                	 ExpertAdvice.setAdvice($translate('advice.second_plowing'));
                    $farmer.load();
                }
            }

            var data = Plantation.save(JSON.stringify($scope.coords),
                $scope.seedling, successPlanting);
            $scope.planting = false;

        };

        $scope.planting = true;
        $scope.dif = 70;
        
        $scope.soilClick = function(p) {
            if ($scope.planting) {
                if (p.active && !p.tree) {
                    if ($scope.availableSeedlings > 0) {
                        // p.tree = "/public/images/game/plant.png";
                        p.tree = "red";
                        p.treeCls = 'seedling no-mouse-event';

                        $scope.availableSeedlings -= $scope.dif;
                        if ($scope.availableSeedlings < 0) {
                            $scope.dif = 35 + $scope.availableSeedlings;
                            $scope.availableSeedlings = 0;
                        }
                        $scope.coords.push({
                            x: p.x,
                            y: p.y
                        });
                    }
                } else if (p.tree) {
                    delete p.tree;
                    delete p.treeCls;
                    $scope.availableSeedlings += $scope.dif;

                    $scope.dif = 35;
                    var oldCoords = $scope.coords;
                    $scope.coords = [];
                    for (var i = 0; i < oldCoords.length; i++) {
                        var el = oldCoords[i];
                        if (el.x == p.x && el.y == p.y) {

                        } else {
                            $scope.coords.push(el);
                        }
                    }
                }
            }
        }

        $scope.treeClick = function(t) {
            if (!$scope.isPlanting) {
            	t.clicked=true;
            }
        };

        $scope.rows = [];
        // var N = 15, M = 5;
        var N = 15,
            M = 5;
        var r = {
            cols: []
        };
        $scope.rows.push(r);
        for (var i = 0; i < N; i++) {
            r.cols.push({
                show: true,
                staticV: true,
                x: i,
                y: j,
                terrain: '/public/images/ograda.png',
                cls: 'ograda-gore'
            })
        }

        for (var j = 0; j < M; j++) {
            var r = {
                cols: []
            };
            $scope.rows.push(r);
            for (var i = 0; i < N; i++) {
                r.cols.push({
                    show: true,
                    active: true,
                    staticV: false,
                    x: i,
                    y: j,
                    terrain: State.gameState().soil_url,
                    cls: 'pocva clickable'
                })

            }
            for (var i = 0; i < N; i++) {
                var p = r.cols[i];
                if (p.active && isInCoords(p)) {
                    // r.cols[i].tree = $scope.$root.farmer.plant_url;
                    r.cols[i].tree = p.plantType;
                    r.cols[i].treeCls = 'seedling no-mouse-event';
                }
            }

        }

    }
]);
