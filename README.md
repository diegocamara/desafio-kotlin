# Desafio Kotlin

Aplicação de example usando microframeworks

## Build

## Run

## REST API
`POST /api/users`
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
```json
{
        "email": "user@user.com",
        "password": "user123"
       
    }
```
`GET /api/users/:id`

### Header

Authorization - Bearer ${userToken}
