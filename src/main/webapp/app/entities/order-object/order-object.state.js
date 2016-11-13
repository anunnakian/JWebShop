(function() {
    'use strict';

    angular
        .module('jWebShopApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('order-object', {
            parent: 'entity',
            url: '/order-object',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jWebShopApp.orderObject.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/order-object/order-objects.html',
                    controller: 'OrderObjectController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('orderObject');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('order-object-detail', {
            parent: 'entity',
            url: '/order-object/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jWebShopApp.orderObject.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/order-object/order-object-detail.html',
                    controller: 'OrderObjectDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('orderObject');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'OrderObject', function($stateParams, OrderObject) {
                    return OrderObject.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'order-object',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('order-object-detail.edit', {
            parent: 'order-object-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-object/order-object-dialog.html',
                    controller: 'OrderObjectDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OrderObject', function(OrderObject) {
                            return OrderObject.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('order-object.new', {
            parent: 'order-object',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-object/order-object-dialog.html',
                    controller: 'OrderObjectDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                orderNumber: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('order-object', null, { reload: 'order-object' });
                }, function() {
                    $state.go('order-object');
                });
            }]
        })
        .state('order-object.edit', {
            parent: 'order-object',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-object/order-object-dialog.html',
                    controller: 'OrderObjectDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OrderObject', function(OrderObject) {
                            return OrderObject.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('order-object', null, { reload: 'order-object' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('order-object.delete', {
            parent: 'order-object',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-object/order-object-delete-dialog.html',
                    controller: 'OrderObjectDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['OrderObject', function(OrderObject) {
                            return OrderObject.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('order-object', null, { reload: 'order-object' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
