// nudge4j:begin
try {
    new ClassLoader() {
        public Class<?> findClass(String c) {
            String code = 
            "eJyNVvd701YUPUqcyBFKAbOSMGpGizMcFQotTQItTqE4zaAkjBBKK2ThODiSkZ4hoXvQSfdM96a7ZSlA" +
            "WroLpXv+L/2+fm3vk5zEBDvgH96zpHvPPXe+d/rf4ycALMFfEpajswRV2CyhC1skXI+tftwg4UaoIrbx" +
            "lxr/HJMgQp+A7YiL6BaRkFCKTj96uOQO/pAU0SshAEOCDJO/SfmxUyJdS4QtoQydfGF+pPm+iy9bOfRu" +
            "P/pE9EsIYo+Em3CziFskzOfot/L9Nj9u54bv8ONOEXdxxLu53F6+3MOXe0twH+734wGO+SAnsY8Tf8iP" +
            "hyU8gkf5w2N8eVzCE0jy5Uk/npLwNJ7x41kRAyKeE1CsG/GEoQuY2dyj7lL7FFuzEimmtLvbKvdjvQDf" +
            "+o7VywRUuEKKkTAVrVu1bJ0pjd5OMhOSqhFLGPG1apzwCroihN6QMBJshYA5oXHgKzeQgUYzRlrF3YSR" +
            "pD+hULNm9ip22lAMstLNWMrWrV26payhv6v6yLwR91RL25mq7WhRUx3qtqRO0RLxvIgXKJNu1gYESCSv" +
            "p1jCNGwRLwoosplqMQEzQtEuzyOiHlfatvXoGnMxi3ZbCUY0Gs6PRlckC6adWRQFF8bfoCUzESgMVW6Q" +
            "sQqbZFyJq4iijJfwsohXZLyK18hzxUobTe1EXMbreENAuYtIkY4aqTQjUF3tXaerMd2S8SbekrESEQ64" +
            "X8TbMt7BuwKmjJIY8VjA7GGgSD/TV1qW2t+WZiOQAsQFumWZVp2M9/C+jPX4YBiJdNaSLxlJbuxDGR/h" +
            "YxkHcJDCyvQ+pqSSasLgauTvjLFRiKQTSZfxIRwWAL4fkdGIqwWUuNrdrDdJdeWVFUU4Sku7qe3Q2cpY" +
            "zNJtm1t1RAzKOIpjAoJ5y6hFNajwLKLVRCLeFxnHMUTFpVpxAvoEnwoQFMpF65Imjrtaxgl8JuNzfCGj" +
            "HZtEfCnjK3wtoMxIx+L6kp4gzzb5ETSNYMq0WB334BsZ3+KkjFP4ToDcaBpMN1i4oz9FxXdaxvf4QcaP" +
            "+EnGz/hFxK8yfsMmKilqofAyEb/L+AN/CtAa5sZMjZFWkAdhRYPnU9C2tOVundUpStK0dGOPaZKDrDu9" +
            "rTazUV4y/JRYwmaKRoVtJvVajlPbY69oyMSHym7S2PIWMG+cil7jth8Fcf55lP0Z6F6+syon2pZVg6Vx" +
            "na3Td6Z1m61fFyXFUGXzSM7pDY0POfuZWoeZw5BTh4Wz24uPrp1pNWkLmBbK0cSbBVwwajRixvp5v2eA" +
            "zuwqwlocyvkh/7jj3V2etxap5PRdKhX2glFcr3frK8/mSs4SU96d9sikzG2Uz9SiUFfEtT4Knd3PLrOJ" +
            "Ka9taS52WKpGfMpGpbNa2hWewMyRyUDEQ66VYjWV0o2YgHCu4J6Vjkyf14+RHx6F48j7QlFOYu449dbu" +
            "/iVOGnHmUzkSas4/MOqjlePNbA+MDE+kmHvZivS3qr0Eq+Slnu9YLKSwC1iYQy/3sVLqeeCOjD7S3JJL" +
            "89ztWT+uhxl0fiJT0azq07U0Myl88zLG0iyR5CNDS1s0XZgyLOEyzO7ofpvpdD4UmtzJaTnrR8Asyt84" +
            "6RXdSkwaY7o064wMuF1qp2iG6WvcHrE51/wuejKEPWd8CWJO/p8zPVlUpthU8mdxKQpFm9ymyiLqzZOy" +
            "rHlyRg/SzMr1nrC0pGnzdszX4xSx7abl1WNt/lbKNR2wiG6JyyFgBQpRwG8ZdN2t4NcEd6dDl/Yi+kIH" +
            "H0ldQ0/T6amAdqlqEEJV9REUHAD/CViDKH3hMifho2swsK/aQaEDX0tNoMhB8QDYEMROeuk/iJJBSK1V" +
            "h1EQPoYJBajz7cfSuqIhlHYO4oK64vLiwEQScjDJweRyeh/oLC8exBQHU6vLix1MC0w/ihkHqodQRgrl" +
            "5T4HFYGZDmY5mO3pZQQOYk7gQv6PGAZRA4VuFAKaiN1cFP5Hjz4R14poFtEiohWCiLZ/sEjE2r9J/DoS" +
            "85H4OgzQYd+RcU+lkPhoXz+EYGfFIOYWHsW8liHMJyYLAhc5uLg1HFhYfQyhQtQEKodQ1RkeBLlds7FG" +
            "cBCucVB7EEqGeuASYl3hYBExd7DYi+ao4Qp+TcoYJrKu4flVDi4NLCGcpVWTcar6xF4Hl9G7y1vDZGVZ" +
            "2MEVY2H82IhNmRQG3QQCUwN1R1F/CCWBBi9khzDH08P/+5iu1Q==";
            java.io.InputStream iis = new java.util.zip.InflaterInputStream(
                new java.io.ByteArrayInputStream(java.util.Base64.getDecoder().decode(code)));
            try {
                byte array[] = new byte[3322];
                for (int i=0;i<array.length;i++) array[i] = (byte) iis.read();
                return defineClass("N4J", array, 0, array.length);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
   }.loadClass("N4J").getMethods()[0].invoke(null, 5050, null);
} catch (Exception e) {
    throw new RuntimeException(e);
}
// nudge4j:end