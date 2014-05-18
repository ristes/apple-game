Game.controller('BuySeadlingsController', ['$scope', '$translate', '$http',
    'Store', 'StoreItems', '$farmer', '$items',
    function($scope, $translate, $http, Store, StoreItems, $farmer, $items) {

      $scope.items = StoreItems['apple-type'];

      $scope.$root.$emit('shop-show', {
        items: $scope.items,
        showNext: false,
        storeUrl: '/public/images/game/sadnici-icon.png'
      });

      $scope.onSelectSize = function(_scope, item) {
        $scope.plantTypeId = item.id;
        $scope.unreg();
        $scope.$root.$emit('shop-hide');
        $scope.$root.$emit('item-bought');
        $scope.items = StoreItems['seedling-type'];
        $scope.$root.$emit('shop-show', {
          items: $scope.items,
          showNext: false,
          storeUrl: '/public/images/game/sadnici-icon.png'
        });
        $scope.unreg = $scope.$root.$on('buy-item', $scope.onBuyItem);

      };

      $scope.onBuyItem = function(_scope, item) {
        Store['buySeedling']({
          seedlingTypeId: item.id,
          plantTypeId: $scope.plantTypeId,
          currentState: "/plantation"
        }, null, function(result) {
          if (result.balans) {
            $items.add('seedlings', item);
            $farmer.swap(result);
            $scope.$root.$emit('shop-hide');
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
