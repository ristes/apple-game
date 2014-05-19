Game.controller('FertilizingController', [
    '$scope',
    '$translate',
    '$http',
    'Store',
    'StoreItems',
    'Operations',
    '$farmer',
    '$items',
    '$plantation',
    '$weather',
    function($scope, $translate, $http, Store, StoreItems, Operations, $farmer,
            $items, $plantation, $weather) {

      var showProgress = function(_scope, oper) {
        $scope.$root.$emit('show-progress-global', {
          title: 'progress.' + oper.name,
          duration: oper.duration
        });
      };


      var onBuyItem = function(item) {
        Store['buyItem']({
          itemid: item.id,
          quantity: item.perHa ? $scope.plantation.area : 1,
          currentState: $scope.$root.farmer.currentState
        }, null, function(result) {
          if (result.balans) {
            $items.add(item.store, item);
            $farmer.swap(result);
            $scope.$root.$emit('shop-hide');
            
            $scope.$root.$emit('show-progress-global', {
              title: 'progress.fertilization',
              duration: 20
            });
          } else {
            $scope.$root.$emit('insuficient-funds');
          }
        }).$promise['finally'](function() {
          $scope.$root.$emit('item-bought');

        });
      };
      var unreg = $scope.$root.$on('operation-fertilizing', function(_s, oper) {
        var hasItem = $items.check('fertilizer');
        if (!hasItem) {
          $scope.$root.$emit('shop-show', {
            items: StoreItems[oper.requires],
            showNext: false,
            storeUrl: '/public/images/game/store-' + oper.requires + '.png',
            onItemClick: onBuyItem
          });

        } else {
          $scope.$root.$emit('show-progress-global', {
            title: 'progress.fertilization',
            duration: 20
          });
        }

      });

      $scope.$on("$destroy", function() {
        if (unreg) {
          unreg();
        }
      });

    }]);