Game.controller('IrrigationController', [
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

      $scope.visible = false;

    }]);