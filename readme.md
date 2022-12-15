# ![logo](resources/alfa.svg) Alfa Converter
Service for converting documents based on their content.   
Supported file formats to conversion:
- [x] `.xlsx` to `.xlsx` using second `.xlsx` as map
- [x] `.xlsx` to `.xml` using `.xsd` scheme of expected `.xml` format

Developed by Mæa Softworks special for Alfa-Bank ❤

## `.xlsx` recognizable data types and formats:
|     Name      |  Type code   |        Format        | Format Code | Status |
|:-------------:|:------------:|:--------------------:|:-----------:|:------:|
|    Boolean    |     `B`      |                      |             |   ✔️   |
|    Number     |     `N`      |     Int, Double      |     `0`     |   ✔️   |
|    Number     |     `N`      |      dd.MM.yyyy      |    `14`     |   ✔️   |
|    Number     |     `N`      |        HH:mm         |    `20`     |   ✔️   |
|    Number     |     `N`      |       H:mm:ss        |    `21`     |   ✔️   |
|    Number     |     `N`      |   dd.MM.yyyy H:mm    |    `22`     |   ✔️   |
|    Number     |     `N`      | _Another formats..._ |     _?_     |   ❌    |
|     Error     |     `E`      |                      |             |   ✔️   |
|       S       |     `S`      |                      |             |   ✔️   |
|      Str      |    `STR`     |                      |             |   ❌    |
| Inline string | `INLINE_STR` |                      |             |   ✔️   |

## `.xsd` recognizable tags:
| Name                                  | Status |
|---------------------------------------|:------:|
| `xsd:all`                             |   ❌    |
| `xsd:annotation`                      |   ❌    |
| `xsd:any`                             |   ❌    |
| `xsd:anyAttribute`                    |   ❌    |
| `xsd:appinfo`                         |   ❌    |
| `xsd:attribute`                       |   ✔️   |
| `xsd:attributeGroup`                  |   ❌    |
| `xsd:complexContent`                  |   ✔️   |
| `xsd:complexType`                     |   ✔️   |
| `xsd:documentation`                   |   ❌    |
| `xsd:element`                         |   ✔️   |
| `xsd:extension` of `simpleContent`    |   ❌    |
| `xsd:extension` of `complexContent`   |   ❌    |
| `xsd:field`                           |   ❌    |
| `xsd:group`                           |   ❌    |
| `xsd:import`                          |   ❌    |
| `xsd:include`                         |   ❌    |
| `xsd:key`                             |   ❌    |
| `xsd:keyref`                          |   ❌    |
| `xsd:list`                            |   🚧   |
| `xsd:notation`                        |   ❌    |
| `xsd:redefine`                        |   ❌    |
| `xsd:restriction` of `simpleType`     |   🚧   |
| `xsd:restriction` of `simpleContent`  |   🚧   |
| `xsd:restriction` of `complexContent` |   🚧   |
| `xsd:schema`                          |   ❌    |
| `xsd:selector`                        |   ❌    |
| `xsd:sequence`                        |   ✔️   |
| `xsd:simpleContent`                   |   🚧   |
| `xsd:simpleType`                      |   🚧   |
| `xsd:union`                           |   🚧   |
| `xsd:unique`                          |   ❌    |