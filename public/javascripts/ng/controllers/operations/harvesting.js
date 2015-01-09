Game.controller('HarvestingController', [
    '$scope',
    '$interval',
    'Harvesting',
    function($scope, $interval, Harvesting) {

        var unreg = $scope.$root.$on('operation-harvest',
            function(_s, oper) {
                $scope.type = oper;

                console.log(oper)
                $scope.apples = [];
                $scope.gameActive = false;
                $scope.basketPosition = [{
                    x: 50
                }];
                $scope.game = {
                    score: 0,
                    totalGood: 0,
                    totalBad: 0,
                    goodCaught: 0,
                    badCaught: 0,
                    basketWidth: 6,
                    timeLeft: 60,
                    dropAppleInterval: 60
                };

                $scope.visible = true;

                $scope.$root.$emit("side-hide");



            });
        Harvesting.shriber(function(data) {
            $scope.harvestingInfo = data;
        });

        $scope.harvest = function(info) {
            $scope.harvestingItem = info;
            $scope.gameActive = true;
        };

        $scope.gameOver = function() {
            console.log("gameOver")
            $scope.gameActive = false;
        }

        $scope.$on("$destroy", function() {
            if (unreg) {
                unreg();
            }
        });

        $scope.hide = function() {
            $scope.visible = false;
        }

    }
]);
