/*
 * Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import 'package:beagle/beagle.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter_test/flutter_test.dart';

const _id = '_beagle_2';
const _type = 'beagle:lazycomponent';
Map<String, dynamic> _child = {
  '_beagleComponent_': 'beagle:text',
  'text': 'Loading the screen via Lazy Component, please wait.',
  'id': '_beagle_3',
};

Map<String, dynamic> _style = {
  'flex': {
    'alignItems': 'CENTER',
  },
  'backgroundColor': 'red',
};
const _contextId = 'contextId';
const _contextValue = 0;
Map<String, dynamic> _properties = {
  '_beagleComponent_': _type,
  'path': '/beagle_global',
  'initialState': {
    '_beagleComponent_': 'beagle:text',
    'text': 'Loading the screen via Lazy Component, please wait.'
  },
  'id': _id,
  'children': [
    _child,
  ],
  'style': _style,
  'context': {
    'id': _contextId,
    'value': _contextValue,
  },
};

void main() {
  group('Given a BeagleUIElement with a properties map', () {
    final beagleUIElement = BeagleUIElement(_properties);

    group('When getId is called', () {
      test('Then it should return the correct value', () {
        final retrievedId = beagleUIElement.getId();
        expect(retrievedId, _id);
      });
    });

    group('When getKey is called', () {
      test('Then it should return the correct value', () {
        const expectedKey = ValueKey(_id);
        final retrievedKey = beagleUIElement.getKey();
        expect(retrievedKey, expectedKey);
      });
    });

    group('When getType is called', () {
      test('Then it should return the correct value', () {
        final retrievedType = beagleUIElement.getType();
        expect(retrievedType, _type);
      });
    });

    group('When getContext is called', () {
      test('Then it should return the correct value', () {
        final retrievedContext = beagleUIElement.getContext();

        expect(retrievedContext.id, _contextId);
        expect(retrievedContext.value, _contextValue);
      });
    });

    group('When hasChildren is called', () {
      test('Then it should return the correct value', () {
        expect(beagleUIElement.hasChildren(), isTrue);
      });
    });

    group('When getChildren is called', () {
      test('Then it should return the correct value', () {
        final retrievedValue = beagleUIElement.getChildren();

        expect(retrievedValue.length, equals(1));
        expect(retrievedValue[0].properties, _child);
      });
    });

    group(
        'When getAttributeValue is called by passing an existent attribute name',
        () {
      test('Then it should return the correct attribute value', () {
        const expectedValue = '/beagle_global';
        final retrievedValue = beagleUIElement.getAttributeValue('path');

        expect(retrievedValue, expectedValue);
      });
    });

    group(
        'When getAttributeValue is called by passing an nonexistent attribute name',
        () {
      test('Then it should return the default value', () {
        const expectedValue = 'default value';
        final retrievedValue =
            beagleUIElement.getAttributeValue('nonexistent', 'default value');

        expect(retrievedValue, expectedValue);
      });
    });

    group('When getStyle is called', () {
      test('Then it should return a BeagleStyle object', () {
        final retrievedValue = beagleUIElement.getStyle();

        expect(retrievedValue.flex.alignItems, AlignItems.CENTER);
        expect(retrievedValue.backgroundColor, 'red');
      });
    });
  });
  group('Given a BeagleUIElement with a properties map without children key',
      () {
    final properties = <String, dynamic>{
      '_beagleComponent_': _type,
      'path': '/beagle_global',
      'initialState': {
        '_beagleComponent_': 'beagle:text',
        'text': 'Loading the screen via Lazy Component, please wait.'
      },
      'id': _id,
    };

    final beagleUIElement = BeagleUIElement(properties);

    group('When hasChildren is called', () {
      test('Then it should return false', () {
        expect(beagleUIElement.hasChildren(), isFalse);
      });
    });

    group('When getChildren is called', () {
      test('Then it should return an empty list', () {
        final expectedValue = <BeagleUIElement>[];
        final retrievedValue = beagleUIElement.getChildren();
        expect(retrievedValue, expectedValue);
      });
    });
  });

  group(
      'Given a BeagleUIElement with a properties map with an empty children list',
      () {
    final properties = <String, dynamic>{
      '_beagleComponent_': _type,
      'path': '/beagle_global',
      'initialState': {
        '_beagleComponent_': 'beagle:text',
        'text': 'Loading the screen via Lazy Component, please wait.'
      },
      'id': _id,
      'children': <dynamic>[]
    };

    final beagleUIElement = BeagleUIElement(properties);

    group('When hasChildren is called', () {
      test('Then it should return false', () {
        expect(beagleUIElement.hasChildren(), isFalse);
      });
    });
  });

  group('Given a BeagleUIElement with a properties map without style key', () {
    final properties = <String, dynamic>{
      '_beagleComponent_': _type,
    };

    final beagleUIElement = BeagleUIElement(properties);

    group('When getStyle is called', () {
      test('Then it should return null', () {
        expect(beagleUIElement.getStyle(), isNull);
      });
    });
  });

  group('Given a BeagleUIElement with a properties map without context key',
      () {
    final properties = <String, dynamic>{
      '_beagleComponent_': _type,
    };

    final beagleUIElement = BeagleUIElement(properties);

    group('When getContext is called', () {
      test('Then it should return null', () {
        expect(beagleUIElement.getContext(), isNull);
      });
    });
  });
}
