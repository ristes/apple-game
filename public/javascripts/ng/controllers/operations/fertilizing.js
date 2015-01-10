Game.controller('FertilizingController', [
    '$scope',
    'Fertilize',
    'State',
    'Planting',
    'Store',
    'BoughtItems',
    '$timeout',
    '$interval',
    '$window',
    function($scope, Fertilize, State, Planting, Store, BoughtItems, $timeout, $interval, $window) {

        $scope.fertilizer = {
            n: 40,
            p: 40,
            k: 40,
            ca: 9,
            mg: 3,
            b: 0.35
        };



        function calculatePrice() {
            var n = $scope.fertilizer;
            var p = $scope.prices;
            $scope.price = n.n * p.n + n.p * p.p + n.k * p.k + n.ca * p.ca + n.b * p.b + n.mg * p.mg;
        }

        $scope.nCfg = {
            range: "max",
            min: 0,
            max: 60,
            onChange: function(n) {
                $scope.fertilizer.n = n;
                calculatePrice();
            }
        };

        $scope.pCfg = {
            range: "max",
            min: 0,
            max: 60,
            onChange: function(n) {
                $scope.fertilizer.p = n;
                calculatePrice();
            }
        };

        $scope.kCfg = {
            range: "max",
            min: 0,
            max: 60,
            onChange: function(n) {
                $scope.fertilizer.k = n;
                calculatePrice();
            }
        };

        $scope.caCfg = {
            range: "max",
            min: 0,
            max: 18,
            onChange: function(n) {
                $scope.fertilizer.ca = n;
                calculatePrice();
            }
        };

        $scope.bCfg = {
            range: "max",
            min: 0,
            max: 1,
            step: 0.05,
            onChange: function(n) {
                $scope.fertilizer.b = n;
                calculatePrice();
            }
        };

        $scope.mgCfg = {
            range: "max",
            min: 0,
            max: 10,
            onChange: function(n) {
                $scope.fertilizer.mg = n;
                calculatePrice();
            }
        };

        $scope.fertilize = function() {

            var interval = 100;
            var time = 10 * 50;

            if (!$scope.fertilizeProgress) {
                Fertilize.fertilize($scope.fertilizer, function() {

                    $scope.status = 0;
                    $scope.fertilizeProgress = true;

                    $interval(function() {
                        $scope.status += 100 / time;
                        $scope.showStatus = Math.round($scope.status);
                        if ($scope.status > 100) {
                            $scope.status = 100;
                        }

                    }, interval, time);

                    $timeout(function() {
                        $scope.fertilizeProgress = false;
                        $scope.hide();
                    }, interval * (time + 1));
                });

            }
        }

        $scope.fertilizationUrl = '/public/images/game/operations/fertilizing.png';

        $scope.buyAnalisys = function() {
            Store.buyItem({
                itemName: 'soil_analyse',
                quantity: 1,
                currentState: State.getByField("farmer").currentState
            }, null, function(result) {
                BoughtItems.load(function() {
                    $scope.analisysItem = BoughtItems.getByName('soil_analyse');
                });
            });
        }

        $scope.hideAnalisys = function() {
            $scope.showingAnalysis = false;
        }
        $scope.showAnalisys = function() {
            Planting.analyseTerain($scope.analisysItem.id,
                function(data) {
                    $scope.analysis = data;
                    $scope.showingAnalysis = true;
                });
        }

        var unreg = $scope.$root.$on('operation-fertilizing', function(_s, oper) {
            $scope.$root.$emit("side-hide");
            $scope.visible = true;

            var size = State.getByField("plantation").area;
            $scope.prices = {
                n: 0.5 * size,
                p: 0.5 * size,
                k: 0.5 * size,
                ca: 50 * size,
                mg: 50 * size,
                b: 50 * size
            }
            calculatePrice();

            $scope.analisysItem = BoughtItems.getByName('soil_analyse');
            if ($scope.analisysItem) {
                $scope.haveAnalisys = true;
            }

            $scope.bgw = $window.innerWidth;
            $scope.bgh = $window.innerHeight;
        });

        $scope.hide = function() {
            $scope.visible = false;

            $scope.bgw = 0;
            $scope.bgh = 0;
        }

        $scope.$on("$destroy", function() {
            if (unreg) {
                unreg();
            }
        });

    }
]);
