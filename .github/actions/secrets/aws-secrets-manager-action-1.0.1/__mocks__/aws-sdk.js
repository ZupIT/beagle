'use strict'

const AWS = jest.createMockFromModule('aws-sdk')

const mockSecrets = {
    my_secret_1: {
        ARN: 'arn:aws:mock-secretsmanager:mock-region:mock-account-id:secret:my_secret_1-XXXXXX',
        VersionId: 'mock-version-id',
        Name: 'my_secret_1',
        SecretString: 'test-value-1',
    },
    my_secret_2: {
        ARN: 'arn:aws:mock-secretsmanager:mock-region:mock-account-id:secret:my_secret_2-XXXXXX',
        VersionId: 'mock-version-id',
        Name: 'my_secret_2',
        SecretString: '{"foo" : "bar"}',
    },
    'my/secret/3': {
        ARN: 'arn:aws:mock-secretsmanager:mock-region:mock-account-id:secret:my/secret/3-XXXXXX',
        VersionId: 'mock-version-id',
        Name: 'my/secret/3',
        SecretBinary: 'eyJmb28iIDogImJhciJ9',
    },
    deleted_secret: {
        ARN: 'arn:aws:mock-secretsmanager:mock-region:mock-account-id:secret:deleted_secret-XXXXXX',
        VersionId: 'mock-version-id',
        Name: 'deleted_secret',
        DeletedDate: Date.now()
    }
}

const keys = Object.keys(mockSecrets)
const values = []
for (var key in mockSecrets) {
    values.push(mockSecrets[key])
}

const mockSecretsManager = jest.fn((secretsManagerConfig) => {
    return {
        getSecretValue: jest.fn(({ SecretId }) => ({
            promise: jest.fn(() => {
                if (SecretId in mockSecrets) {
                    return Promise.resolve(mockSecrets[SecretId])
                } else {
                    return Promise.reject(new Error('SecretId not in mockSecrets'))
                }
            })
        })),
        listSecrets: jest.fn().mockReturnValue({
            promise: jest.fn().mockResolvedValue({
                SecretList: values
            })
        })
    }
})

AWS.SecretsManager = mockSecretsManager

module.exports = AWS