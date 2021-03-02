/*
 * Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA;
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at;
 *
 *     http://www.apache.org/licenses/LICENSE-2.0;
 *
 * Unless required by applicable law or agreed to in writing, software;
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and;
 * limitations under the License.
 */

import 'package:beagle/default/url_builder.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  group('Given an UrlBuilder with a simple base url', () {
    const baseURL = 'http://base.url';
    final urlBuilder = UrlBuilder(baseURL);
    group('When build method is called', () {
      test('Then it should prepend baseUrl if path is relative', () {
        const path = '/relativePath';
        expect(urlBuilder.build(path), '$baseURL$path');
      });

      test('Then it should not add baseUrl if path is not relative', () {
        const absolutePath = 'absolutePath';
        expect(urlBuilder.build(absolutePath), absolutePath);
      });

      test('Then it should handle multiple / as a relative path', () {
        const path = '//weirdPath';
        expect(urlBuilder.build(path), '$baseURL$path');
      });

      test('Then it should handle root path', () {
        const path = '/';
        expect(urlBuilder.build(path), '$baseURL$path');
      });

      test('Then it should return path if path is empty', () {
        const path = '';
        expect(urlBuilder.build(path), '');
      });
    });

    group('Given an UrlBuilder with a base url ending with /', () {
      const baseURL = 'http://base.ending.with.slash/';
      final urlBuilder = UrlBuilder(baseURL);
      group('When build method is called', () {
        test(
            'Then it should not duplicate / when both baseUrl and path have it',
            () {
          const path = '/relativePath';
          expect(urlBuilder.build(path),
              'http://base.ending.with.slash/relativePath');
        });
      });
    });

    group('Given an UrlBuilder with an empty baseURL', () {
      final urlBuilder = UrlBuilder('');

      group('When build method is called', () {
        test('Then it should handle relative path when baseURL is empty', () {
          const path = '/relativePath';
          expect(urlBuilder.build(path), path);
        });

        test('Then it should handle absolute path when baseURL is empty', () {
          const absolutePath = 'absolutePath';
          expect(urlBuilder.build(absolutePath), absolutePath);
        });

        test('Then it should handle empty path when baseURL is empty', () {
          const path = '';
          expect(urlBuilder.build(path), '');
        });
      });
    });

    group('Given an UrlBuilder', () {
      final urlBuilder = UrlBuilder('');

      group('When build method is called with an encoded url', () {
        test('Then it should not encode URL', () {
          const encoded =
              'https://www.guiaviagensbrasil.com/imagens/Imagem%20do%20mar%20calma%20e%20belo%20da%20Praia%20da%20Engenhoca-Itacare-Bahia-BA.jpg';
          expect(urlBuilder.build(encoded), encoded);
        });
      });

      group('When build method is called with an uncoded url', () {
        test('Then it should encode URL', () {
          const original =
              'https://www.guiaviagensbrasil.com/imagens/Imagem do mar calma e belo da Praia da Engenhoca-Itacare-Bahia-BA.jpg';
          const encoded =
              'https://www.guiaviagensbrasil.com/imagens/Imagem%20do%20mar%20calma%20e%20belo%20da%20Praia%20da%20Engenhoca-Itacare-Bahia-BA.jpg';
          expect(urlBuilder.build(original), encoded);
        });
      });

      group('When build method is called with a url with diacritics', () {
        test('Then it should encode URL with diacritics', () {
          const original =
              'https://www.guiaviagensbrasil.com/imagens/Imagem do mar calma e belo da Praia da Engenhoca-Itacaré-Bahia-BA.jpg';
          const encoded =
              'https://www.guiaviagensbrasil.com/imagens/Imagem%20do%20mar%20calma%20e%20belo%20da%20Praia%20da%20Engenhoca-Itacar%C3%A9-Bahia-BA.jpg';
          expect(urlBuilder.build(original), encoded);
        });
      });
    });
  });
}
