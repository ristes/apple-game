Game.controller('HarvestingController', [
    '$scope',
    '$interval',
    'Harvesting',
    'Fridge',
    function($scope, $interval, Harvesting, Fridge) {

        $scope.storeHarvest = false;
        $scope.gameActive = false;

        var unreg = $scope.$root.$on('operation-harvest',
            function(_s, oper) {
                $scope.type = oper;

                $scope.storeHarvest = false;
                $scope.gameActive = false;
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
                    basketWidth: 6,
                    timeLeft: 60,
                    dropAppleInterval: 60
                };

                $scope.visible = true;

                $scope.$root.$emit("side-hide");

                Harvesting.shriber(function(data) {
                    $scope.harvestingInfo = data;
                });
            });


        $scope.harvest = function(info) {
            $scope.harvest.info = info;
            //$scope.gameActive = true;
            $scope.gameOver();

        };

        $scope.gameOver = function(result) {
            $scope.gameActive = false;
            // TODO: call harvest service with the result
            Harvesting.harvest(40, 50, 5, 20, $scope.harvest.info.plantationSeedlignId);
            $scope.storeHarvest = true;
            $scope.fridges = Fridge.load();

            $scope.storeCfg = {
                range: "max",
                min: 1,
                max: 100,
                slide: function(event, ui) {
                    $scope.$apply(function() {
                        $scope.harvest.quantity = ui.value;
                    });
                }
            };
        };

        $scope.$on("$destroy", function() {
            if (unreg) {
                unreg();
            }
        });

        $scope.hide = function() {
            $scope.visible = false;
        };

        $scope.buyCapacity = function(fridge) {
            $scope.fridges = Fridge.buyCapacity(fridge.fridgeType, $scope.fridgesCapacity[fridge.fridgeType].capacity);
        };

        $scope.store = function(fridge) {
            var quantity = $scope.harvest.quantity;
            var plantType = $scope.harvest.info.type.id;

            $scope.fridges = Fridge.addtofridge(fridge.fridgeType, plantType, quantity);
            $scope.storeCfg.max = $scope.storeCfg.max - quantity;
            $scope.harvest.quantity = 0;
        };

        $scope.changeQuantity = function(shelf) {
            if (!shelf.changeQuantity) {
                shelf.changeQuantity = true;
                shelf.sliderQuantity = 0;
                shelf.cfg = {
                    range: "max",
                    min: 1,
                    max: shelf.quantity,
                    slide: function(event, ui) {
                        $scope.$apply(function() {
                            shelf.sliderQuantity = ui.value;
                        });
                    }
                }
            }
        };

        $scope.removeQuantity = function(shelf) {
            shelf.quantity -= shelf.sliderQuantity;
            $scope.harvest.quantity += shelf.sliderQuantity;
            shelf.sliderQuantity = 0;
            shelf.changeQuantity = false;
        };

        $scope.fridgesCapacity = [{
            capacity: 0
        }, {
            capacity: 0
        }, {
            capacity: 0
        }];

        $scope.cfg = [{
            range: "max",
            min: 1,
            max: 100,
            slide: function(event, ui) {
                $scope.$apply(function() {
                    $scope.fridgesCapacity[0].capacity = ui.value;
                });
            }
        }, {
            range: "max",
            min: 1,
            max: 100,
            slide: function(event, ui) {
                $scope.$apply(function() {
                    $scope.fridgesCapacity[1].capacity = ui.value;
                });
            }
        }, {
            range: "max",
            min: 1,
            max: 100,
            slide: function(event, ui) {
                $scope.$apply(function() {
                    $scope.fridgesCapacity[2].capacity = ui.value;
                });
            }
        }];

    }
]);
