import {useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import {useAuth} from "../components/AuthContext";
import {useTheme} from "../components/ThemeContext";

const LoginPage = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();
    const {login} = useAuth();
    const {isDarkMode} = useTheme();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError("");
        setIsLoading(true);

        try {
            await login(email, password);
            navigate("/");
        } catch (err: any) {
            setError(err.message || "Failed to login. Please try again.");
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className={`min-h-screen flex items-center justify-center ${isDarkMode ? "bg-gray-900" : "bg-gray-50"}`}>
            <div className="max-w-md w-full space-y-8">
                <div>
                    <h2
                        className={`mt-6 text-center text-3xl font-extrabold ${
                            isDarkMode ? "text-white" : "text-gray-900"
                        }`}
                    >
                        Sign in to your account
                    </h2>
                </div>
                <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
                    <div className="rounded-md shadow-sm space-y-4">
                        <div>
                            <label htmlFor="email" className="sr-only">
                                Email address
                            </label>
                            <input
                                id="email"
                                name="email"
                                type="email"
                                autoComplete="email"
                                required
                                className={`appearance-none rounded relative block w-full px-3 py-2 border ${
                                    isDarkMode
                                        ? "border-gray-600 bg-gray-700 text-white placeholder-gray-400 focus:ring-indigo-500 focus:border-indigo-500"
                                        : "border-gray-300 bg-white text-gray-900 placeholder-gray-500 focus:ring-indigo-500 focus:border-indigo-500"
                                } focus:outline-none focus:z-10 sm:text-sm`}
                                placeholder="Email address"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                disabled={isLoading}
                            />
                        </div>
                        <div>
                            <label htmlFor="password" className="sr-only">
                                Password
                            </label>
                            <input
                                id="password"
                                name="password"
                                type="password"
                                autoComplete="current-password"
                                required
                                className={`appearance-none rounded relative block w-full px-3 py-2 border ${
                                    isDarkMode
                                        ? "border-gray-600 bg-gray-700 text-white placeholder-gray-400 focus:ring-indigo-500 focus:border-indigo-500"
                                        : "border-gray-300 bg-white text-gray-900 placeholder-gray-500 focus:ring-indigo-500 focus:border-indigo-500"
                                } focus:outline-none focus:z-10 sm:text-sm`}
                                placeholder="Password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                disabled={isLoading}
                            />
                        </div>
                    </div>
                    {error && (
                        <div className={`rounded-md p-4 ${isDarkMode ? "bg-red-900" : "bg-red-50"}`}>
                            <div className="flex">
                                <div className="ml-3">
                                    <h3
                                        className={`text-sm font-medium ${
                                            isDarkMode ? "text-red-200" : "text-red-800"
                                        }`}
                                    >
                                        {error}
                                    </h3>
                                </div>
                            </div>
                        </div>
                    )}
                    <div>
                        <button
                            type="submit"
                            disabled={isLoading}
                            className={`group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 ${
                                isDarkMode ? "focus:ring-indigo-400" : "focus:ring-indigo-500"
                            } disabled:opacity-50 disabled:cursor-not-allowed`}
                        >
                            {isLoading ? "Signing in..." : "Sign in"}
                        </button>
                    </div>

                    <p className={`mt-2 text-center text-sm ${isDarkMode ? "text-gray-300" : "text-gray-600"}`}>
                        Don't have an account?{" "}
                        <Link
                            to="/signup"
                            className={`font-medium ${
                                isDarkMode
                                    ? "text-indigo-400 hover:text-indigo-300"
                                    : "text-indigo-600 hover:text-indigo-500"
                            }`}
                        >
                            Sign up here
                        </Link>
                    </p>
                </form>
            </div>
        </div>
    );
};

export default LoginPage;
