Game.controller('DeepPlowingController', [
    '$scope',
    '$translate',
    '$http',
    'Store',
    'StoreItems',
    'Operations',
    '$farmer',
    '$items',
    '$weather',
    '$plowing',
    function($scope, $translate, $http, Store, StoreItems, Operations, $farmer,
            $items, $weather, $plowing) {

      var unreg = $scope.$root.$on('operation-deep-plowing', function(_s, oper) {

        $scope.$root.$emit('show-progress-global', {
          title: 'progress_deep_plowing',
          duration: 10
        });
        $plowing.deepPlowing();
      });

      $scope.$on("$destroy", function() {
        if (unreg) {
          unreg();
        }
      });

    }]);