Game.controller('BuyTerrainController', ['$scope', '$farmer', 'Terrain','ExpertAdvice','$translate','$filter',
    function($scope, $farmer, Terrain,ExpertAdvice, $translate, $filter) {
	
      $scope.items = Terrain.terrainSizes;
      ExpertAdvice.setAdvice($translate("buy_terrain.advice"));
      $scope.onHover = function(item) {
    	  ExpertAdvice.setAdvice(item.description);
      }
      $scope.$root.$emit('shop-show', {
        items: $scope.items,
        showNext: false,
        shop: {
          name: 'Land size'
        },
        storeUrl: '/public/images/game/stores/terrain.png'
      });

      $scope.onSelectSize = function(_scope, item) {
        $scope.size = item.size;
        $scope.unreg();
        $scope.$root.$emit('shop-hide');
        $scope.$root.$emit('item-bought');
        $scope.items = Terrain.terrains;
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
        Terrain.buyTerrain({
          terrainId: item.id,
          size: $scope.size,
          price: item.price,
          currentState: "/buy_base"
        }, null, function(result) {
          if (result.farmer.balans) {
            $scope.$root.$emit('shop-hide');
            $farmer.swap(result.farmer);
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