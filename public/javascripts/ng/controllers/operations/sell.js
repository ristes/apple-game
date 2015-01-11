Game.controller('SellController', [
    '$scope',
    'Fridge',
    'State',
    'Harvesting',
    function($scope, Fridge, State, Harvesting) {

        var SliderCfg = function(max, min) {
            this.range = "max";
            this.max = max || 0;
            this.min = min || 0;
            this.quantity = 0;
            var self = this;

            this.slide = function(event, ui) {
                $scope.$apply(function() {
                    self.quantity = ui.value;
                });
            }
        };

        var unreg = $scope.$root.$on('operation-sell', function(_s, oper) {
            $scope.type = {
                ico: '/public/images/game/operations/shop.png',
                name: 'sell'
            };

            $scope.storeCfg = [];


            $scope.visible = true;
            $scope.$root.$emit("side-hide");
            $scope.plantation = State.getByField("plantation");

            for (var i = 0; i < $scope.plantation.plantTypes.length; i++) {
                var type = $scope.plantation.plantTypes[i];
                $scope.storeCfg[type.name] = new SliderCfg(0)
            }

            $scope.inintStoring();
        });


        $scope.inintStoring = function() {

            $scope.fridges = Fridge.load();

            Harvesting.getYield(function(data) {
                $scope.yields = data;
                for (var i = 0; i < data.length; i++) {
                    var
                    yield = data[i];
                    var plantTypeName =
                        yield.plantationSeedling.seedling.type.name;

                    $scope.storeCfg[plantTypeName] = new SliderCfg(
                        yield.quantity -
                        yield.storedQuantity);
                }
            });
        };



        $scope.$on("$destroy", function() {
            if (unreg) {
                unreg();
            }
        });

        $scope.buyCapacity = function(fridge) {
            Fridge.buyCapacity(fridge.fridgeType, $scope.fridgesCapacity[fridge.fridgeType].capacity, $scope.inintStoring);
        };

        $scope.store = function(fridge, plantType, cfg) {
            var quantity = cfg.quantity;
            var plantType = plantType.id;

            Fridge.addtofridge(fridge.fridgeType, plantType, quantity, $scope.inintStoring);
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

        $scope.sell = function(type, cfg) {
            Harvesting.sell(type.id, cfg.quantity, $scope.inintStoring);
        };

        $scope.removeQuantity = function(shelf, fridge) {
            shelf.quantity -= shelf.sliderQuantity;
            Fridge.removeFromFridge(fridge.fridgeType, shelf.plantType.id, shelf.sliderQuantity, $scope.inintStoring);
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

        $scope.hide = function() {
            $scope.visible = false;
        };

    }
]);
