Game.controller('PlowingController', [
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

      var unreg = $scope.$root.$on('operation-plowing', function(_s, oper) {
        $scope.$root.$emit('show-progress-global', {
          title: 'progress.plowing',
          duration: 30
        });

      });

      $scope.$on("$destroy", function() {
        if (unreg) {
          unreg();
        }
      });

    }]);