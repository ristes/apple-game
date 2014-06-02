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

      $scope.quantity = 1;

      var onBuyItem = function(item) {
        Store['buyItem']({
          itemName: item.name,
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
            $items.use(item.store);
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
            showNext: true,
            storeUrl: oper.ico,
            onItemClick: onBuyItem
          });

        } else {
          var types = $items.get(oper.requires);
          if (types.length > 1) {
            $scope.$root.$emit('shop-show', {
              items: types,
              showNext: true,
              storeUrl: oper.ico,
              shop: {
                name: oper.requires
              },
              onItemClick: function(t) {
                $scope.type = t;
                $scope.$root.$emit('shop-hide');
                $scope.$root.$emit('show-progress-global', {
                  title: 'progress.fertilization',
                  duration: 20
                });
                $items.use(oper.requires, $scope.type);
              }
            });
          } else {
            $scope.type = types[0];
            $scope.$root.$emit('show-progress-global', {
              title: 'progress.fertilization',
              duration: 20
            });
            $items.use(oper.requires, $scope.type);
          }
        }

      });

      $scope.$on("$destroy", function() {
        if (unreg) {
          unreg();
        }
      });

    }]);