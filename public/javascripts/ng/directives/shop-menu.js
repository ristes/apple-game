GameDirectives.directive('shopMenu', [
    'jQuery',
    'BoughtItems',
    'State',
    'ExpertAdvice',
    function($, BoughtItems, State, ExpertAdvice) {
        return {
            restrict: 'E',
            transclude: true,
            scope: {},
            link: function(scope, element, attrs, ctrl, transclude, formCtrl) {
                scope.showNext = true;
                scope.visible = false;
                scope.buying = false;

                scope.itemClick = function(item) {
                    if (!scope.buying) {
                        scope.buying = true;
                        scope.$root.$emit('buy-item', item);
                        if (scope.onItemClick) {
                            scope.onItemClick(item);
                        }
                    }
                };
                
                scope.onHover = function(item) {
                	ExpertAdvice.setAdvice(item.description);
                }
                scope.onHide = function() {
                	ExpertAdvice.hide();
                }
                scope.executeItem = function(item) {
                    console.log("Item bought");
                }

                var unregBought = scope.$root.$on("item-bought", function($scope) {
                    if (State.gameState().field) {
                        BoughtItems.load();
                    }
                    scope.buying = false;
                });

                var unregShow = scope.$root.$on("shop-show", function($scope, cfg) {
                    scope.$root.$emit('side-hide');
                    scope.items = cfg.items;
                    scope.shop = cfg.shop;
                    scope.storeUrl = cfg.storeUrl;
                    if (cfg.showNext !== null) {
                        scope.showNext = cfg.showNext;
                        if (cfg.onHide) {
                            scope.hideFn = cfg.onHide;
                        }
                    }
                    if (cfg.onItemClick) {
                        scope.onItemClick = cfg.onItemClick;
                    }
                    scope.visible = true;
                });

                scope.price = function(item) {
                    scope.plantation = State.getByField("plantation");
                    if (item.perHa && item.price && scope.plantation) {
                        return item.price * scope.plantation.area;
                    } else {
                        return item.price;
                    }
                }

                var unregHide = scope.$root.$on("shop-hide", function() {
                    scope.hide();
                });

                scope.hide = function() {
                    scope.visible = false;
                    scope.buying = false;
                    if (scope.hideFn) {
                        scope.hideFn();
                    }
                }

                scope.$on("$destroy", function() {
                    unregBought();
                    unregHide();
                    unregShow();
                });

            },
            templateUrl: '/public/templates/shop-menu.html'
        };

    }
])
