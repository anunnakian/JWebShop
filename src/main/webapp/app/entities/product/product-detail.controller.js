(function() {
    'use strict';

    angular
        .module('jWebShopApp')
        .controller('ProductDetailController', ProductDetailController);

    ProductDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Product', 'Category'];

    function ProductDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Product, Category) {
        var vm = this;

        vm.product = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('jWebShopApp:productUpdate', function(event, result) {
            vm.product = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
