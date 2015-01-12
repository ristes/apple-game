Game.controller('ClothingController', ['$scope', function($scope) {

    var unreg = $scope.$root.$on('operation-pruning', function(_s, oper) {
        $scope.type = oper;
        $scope.gameActive = true;
        $scope.visible = true;
        $scope.$root.$emit("side-hide");
    });

    $scope.gameOver = function(gameResult) {
        $scope.gameActive = false;
        // TODO: call the clothing service with the result
    }

    $scope.$on("$destroy", function() {
        if (unreg) {
            unreg();
        }
    });



}]);
