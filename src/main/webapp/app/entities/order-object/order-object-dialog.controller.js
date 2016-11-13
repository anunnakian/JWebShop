(function() {
    'use strict';

    angular
        .module('jWebShopApp')
        .controller('OrderObjectDialogController', OrderObjectDialogController);

    OrderObjectDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'OrderObject', 'OrderLine'];

    function OrderObjectDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, OrderObject, OrderLine) {
        var vm = this;

        vm.orderObject = entity;
        vm.clear = clear;
        vm.save = save;
        vm.orderlines = OrderLine.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.orderObject.id !== null) {
                OrderObject.update(vm.orderObject, onSaveSuccess, onSaveError);
            } else {
                OrderObject.save(vm.orderObject, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jWebShopApp:orderObjectUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
