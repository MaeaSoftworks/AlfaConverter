# How to inject

1. Initialization
   ```java
   import com.maeasoftworks.alfaconverter.ConverterContainer;
   import com.maeasoftworks.alfaconverter.model.BondedPair;
   
   ConverterContainer container = new ConverterContainer();
   ```

2. Get headers
   ```java
   container.getHeaders(file1, file2);
   ```
   where `file1` and `file2` is `byte[]` of files (must be provided by frontend).

3. Create Converter in full mode
   ```java
   container.initialize(file1, file2);
   ```
   `file1` and `file2` is same `byte[]` from stage 2.

4. Set file order (source file and structure file)
   **Should be set after reinitialization and before conversion!**
   ```java
   container.setHeadship(0 /*or 1*/);
   ```
   or
   ```java
   container.setHeadship(BondedPair.Headship.FIRST /*or SECOND*/);
   ```

5. Set conversion rules (must be provided by frontend)
   ```java
   String data; //provided by frontend
   container.setConversion(data);
   ```

6. Convert!
   ```java
   byte[] result = container.convert();
   ```