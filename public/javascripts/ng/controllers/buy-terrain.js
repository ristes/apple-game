Game.controller('BuyTerrainController', ['$scope', '$translate', '$http',
    'Store', 'StoreItems', '$farmer',
    function($scope, $translate, $http, Store, StoreItems, $farmer) {
      $scope.items = $scope.$root.storeItems['terrain-size'];

      $scope.$root.$emit('shop-show', {
        items: $scope.items,
        showNext: false,
        shop: {
          name: 'terrainSize'
        },
        storeUrl: '/public/images/game/stores/terrain.png'
      });

      $scope.onSelectSize = function(_scope, item) {
        $scope.size = item.size;
        $scope.unreg();
        $scope.$root.$emit('shop-hide');
        $scope.$root.$emit('item-bought');
        $scope.items = $scope.$root.storeItems['terrain'];
        $scope.$root.$emit('shop-show', {
          items: $scope.items,
          showNext: false,
          shop: {
            name: 'terrainType'
          },
          storeUrl: '/public/images/game/stores/terrain.png'
        });
        $scope.unreg = $scope.$root.$on('buy-item', $scope.onBuyItem);

      };

      $scope.onBuyItem = function(_scope, item) {
        Store['buyTerrain']({
          terrainId: item.id,
          size: $scope.size,
          price: item.price,
          currentState: "/buy_base"
        }, null, function(result) {
          if (result.balans) {
            $scope.$root.$emit('shop-hide');
            $farmer.swap(result);
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