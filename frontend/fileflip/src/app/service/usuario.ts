export class Usuario {
    
    constructor(
        private nome: string,
        private email: string,
        private telefone: string,
        private foto_perfil: string
    ) {}

    getNome(): string {
        return this.nome;
    }

    getEmail(): string {
        return this.email;
    }

    getTelefone(): string {
        return this.telefone;
    }

    getFotoPerfil(): string {
        return this.foto_perfil;
    }


    setNome(novoNome: string): void {
        this.nome = novoNome;
    }

    setEmail(novoEmail: string): void {
        this.email = novoEmail;
    }

}