Game.controller('SellController', ['$scope', 'Fridge', function($scope, Fridge) {

    var unreg = $scope.$root.$on('operation-sell', function(_s, oper) {
        $scope.type = {
            ico: '/public/images/game/operations/shop.png',
            name: 'sell'
        };
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
        $scope.visible = true;
        $scope.$root.$emit("side-hide");
    });

    $scope.hide = function() {
        $scope.visible = false;
    };

    $scope.$on("$destroy", function() {
        if (unreg) {
            unreg();
        }
    });

    $scope.harvest = {};

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

}]);
