(function() {
    'use strict';

    angular
        .module('jWebShopApp')
        .controller('OrderLineDialogController', OrderLineDialogController);

    OrderLineDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'OrderLine', 'OrderObject', 'Product'];

    function OrderLineDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, OrderLine, OrderObject, Product) {
        var vm = this;

        vm.orderLine = entity;
        vm.clear = clear;
        vm.save = save;
        vm.orderobjects = OrderObject.query();
        vm.products = Product.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.orderLine.id !== null) {
                OrderLine.update(vm.orderLine, onSaveSuccess, onSaveError);
            } else {
                OrderLine.save(vm.orderLine, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jWebShopApp:orderLineUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
