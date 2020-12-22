function test() {
    let current = 0

    return {
        next: () => current++,
        json: () => ({
            string: 'hello world',
            integer: current,
            float: 0.5478 + current,
            bool: true,
            stringArray: ['hello', 'world'],
            nil: null,
            integerArray: [current - 1, current, current + 1],
            floatArray: [current - 1.2445, current + 0.001, current + 1.7812],
            boolArray: [true, false, true],
            mixedArray: ['hello', current, undefined, current + 1, null, true],
            object: {
                hello: 'world',
                integer: current,
                sub: {
                    float: current + 0.4122,
                    array: [1, 2, '3', { test: 'crazy stuff' }]
                }
            }
        })
    }
}

let counter = 0

const global = {
    test: test(),
    view: {
        _beagleComponent_: 'beagle:container',
        children: [
            {
                _beagleComponent_: 'beagle:text',
                text: 'Hello World!'
            }
        ]
    },
    update: () => {
        const view = {
            _beagleComponent_: 'beagle:container',
            children: [
                {
                    _beagleComponent_: 'beagle:text',
                    text: `Hello World ${++counter}!`
                }
            ]
        }
        sendMessage('someChannelName', JSON.stringify(view))
        return 'ok'
    }
}
