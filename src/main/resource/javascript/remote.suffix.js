function getEncryptedPassword(password,servertime,nonce,rsaPubkey){var RSAKey=new sinaSSOEncoder.RSAKey;RSAKey.setPublic(rsaPubkey,"10001");var encryptedPassword=RSAKey.encrypt([servertime,nonce].join("\t")+"\n"+password);return encryptedPassword;}