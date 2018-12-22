# Desafio Kotlin

Aplicação de example usando microframeworks

## Build
``` ./gradle build```
## Run
``` ./gradle run```
### Test
``` ./gradle test```

## REST API
`POST /api/users`

Formato:
```json
{   
    "name": "Username",
    "email": "user@user.com",
    "password": "user123",
    "phones": [
        {
            "number": "987654321",
            "ddd": "21"
        }
    ]
}

```

`POST /api/login`

Formato:
```json
{
    "email": "user@user.com",
    "password": "user123"
}
```
`GET /api/users/:id`

O endpoint GET /api/users/:id requer header de autenticação 

### Header

Authorization - Bearer ${userToken}
