Game.controller('BuyTerrainController',
        [
            '$scope',
            '$translate',
            '$http',
            'Store',
            'StoreItems',
            '$plantation',
            '$farmer',
            function($scope, $translate, $http, Store, StoreItems, $plantation,
                    $farmer) {
              $scope.items = StoreItems['terrain-size'];

              $scope.$root.$emit('shop-show', {
                items: $scope.items,
                showNext: false,
                storeUrl: '/public/images/game/pocva-prodavnica-icon.png'
              });

              $scope.onSelectSize = function(_scope, item) {
                $scope.size = item.size;
                $scope.unreg();
                $scope.$root.$emit('shop-hide');
                $scope.$root.$emit('item-bought');
                $scope.items = StoreItems['terrain'];
                $scope.$root.$emit('shop-show', {
                  items: $scope.items,
                  showNext: false,
                  storeUrl: '/public/images/game/pocva-prodavnica-icon.png'
                });
                $scope.unreg = $scope.$root.$on('buy-item', $scope.onBuyItem);

              };

              $scope.onBuyItem = function(_scope, item) {
                Store['buyTerrain']({
                  terrainId: item.id,
                  size: $scope.size,
                  currentState: "/buy_base"
                }, null, function(result) {
                  if (result.balans) {
                    $scope.$root.$emit('shop-hide');
                    $farmer.swap(result);
                    $plantation.load();
                  } else {
                    $scope.$root.$emit('insuficient-funds');
                  }
                }).$promise['finally'](function() {
                  $scope.$root.$emit('item-bought');
                });
              };

              $scope.unreg = $scope.$root.$on('buy-item', $scope.onSelectSize);

              $scope.$on("$destroy", function() {
                $scope.unreg();
              })

            }]);