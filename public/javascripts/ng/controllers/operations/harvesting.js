Game.controller('HarvestingController', [
    '$scope',
    '$interval',
    'Harvesting',
    'Fridge',
    'State',
    function($scope, $interval, Harvesting, Fridge, State) {

        $scope.gameActive = false;

        var unreg = $scope.$root.$on('operation-harvest',
            function(_s, oper) {
                $scope.type = oper;

                $scope.gameActive = false;
                $scope.apples = [];
                $scope.basketPosition = [{
                    x: 50
                }];
                $scope.game = {
                    score: 0,
                    totalGood: 0,
                    totalBad: 0,
                    goodCaught: 0,
                    badCaught: 0,
                    basketWidth: 10,
                    timeLeft: 60,
                    dropAppleInterval: 30
                };

                $scope.visible = true;

                $scope.$root.$emit("side-hide");

                Harvesting.shriber(function(data) {
                    if (data && data.length) {
                        $scope.harvestingInfo = data;
                    } else {
                        $scope.harvestingInfo = null;
                    }
                });
            });



        $scope.harvest = function(info) {
            $scope.harvest.info = info;
            $scope.gameActive = true;
        };

        $scope.gameOver = function(result) {
            $scope.gameActive = false;
            Harvesting.harvest({
                goodcollected: result.goodCaught,
                goodtotal: result.totalGood,
                badcollected: result.badCaught,
                badtotal: result.totalBad,
                plantationseedling: $scope.harvest.info.plantationSeedlignId
            }, function() {
                Harvesting.shriber(function(data) {
                    if (data && data.length) {
                        $scope.harvestingInfo = data;
                    } else {
                        $scope.harvestingInfo = null;
                    }
                });
            });
        };

        $scope.$on("$destroy", function() {
            if (unreg) {
                unreg();
            }
        });

        $scope.hide = function() {
            $scope.visible = false;
        };


        $scope.sell = function() {
            $scope.hide();
            $scope.$root.$emit('operation-sell');
        };
    }
]);
