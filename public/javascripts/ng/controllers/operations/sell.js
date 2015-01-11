Game.controller('SellController', [
    '$scope',
    'Fridge',
    'State',
    function($scope, Fridge, State) {

        var unreg = $scope.$root.$on('operation-sell', function(_s, oper) {
            $scope.type = {
                ico: '/public/images/game/operations/shop.png',
                name: 'sell'
            };
            $scope.fridges = Fridge.load();

            $scope.storeCfg = [];


            $scope.visible = true;
            $scope.$root.$emit("side-hide");
            $scope.plantation = State.getByField("plantation");

            var SliderCfg = function(max, min) {
                this.range = "max";
                this.max = max || 100;
                this.min = min || 1;
                this.quantity = 0;
                var self = this;

                this.slide = function(event, ui) {
                    $scope.$apply(function() {
                        self.quantity = ui.value;
                    });
                }
            };

            // TODO: get the unstored amounts
            for (var i = 0; i < $scope.plantation.plantTypes.length; i++) {
                var type = $scope.plantation.plantTypes[i];
                $scope.storeCfg[type.name] = new StoreCfg(100)
            }
        });

        $scope.hide = function() {
            $scope.visible = false;
        };

        $scope.$on("$destroy", function() {
            if (unreg) {
                unreg();
            }
        });

        $scope.buyCapacity = function(fridge) {
            $scope.fridges = Fridge.buyCapacity(fridge.fridgeType, $scope.fridgesCapacity[fridge.fridgeType].capacity);
        };

        $scope.store = function(fridge, plantType, cfg) {
            var quantity = cfg.quantity;
            var plantType = plantType.id;

            $scope.fridges = Fridge.addtofridge(fridge.fridgeType, plantType, quantity);
            $scope.storeCfg[plantType.name].max -= quantity;
            $scope.storeCfg[plantType.name].quantity = 0;
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
            $scope.storeCfg[shelf.plantType.name].max += shelf.sliderQuantity;
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
