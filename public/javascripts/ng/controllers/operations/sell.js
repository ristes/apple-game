Game.controller('SellController', ['$scope', function($scope) {

    var unreg = $scope.$root.$on('operation-sell', function(_s, oper) {
        $scope.type = {
            ico: '/public/images/game/operations/shop.png',
            name: 'sell'
        };
        $scope.gameActive = true;
        $scope.visible = true;
        $scope.$root.$emit("side-hide");
    });

    $scope.$on("$destroy", function() {
        if (unreg) {
            unreg();
        }
    });



}]);
