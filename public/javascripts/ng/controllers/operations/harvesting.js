Game.controller('HarvestingController', [
    '$scope',
    '$translate',
    '$http',
    'Store',
    'StoreItems',
    'Operations',
    '$farmer',
    '$items',
    '$weather',
    '$day',
    function($scope, $translate, $http, Store, StoreItems, Operations, $farmer,
            $items, $weather, $day) {

      // TODO: open panel to choose weather to play the game or not

      // not playing => call service with score = 0.7
      // otherwise show game panel, and disable clicking anywhere else

    }]);
