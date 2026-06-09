import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { NavLink } from "react-router-dom";
import CustomAlert from "../../layout/CustomAlert";
export default function ViewEditAccount() {
  const [alldata, setAlldata] = useState([]);
  const [searchBy, setsearchBy] = useState("");
  const [checkpoint, setCheckpoint] = useState(0);
  const [searchKeyword, setSearchKeyword] = useState("");

  // Function to handle changes in the search input field
  const handleSearch = (event) => {
    setSearchKeyword(event.target.value);
  };
  // Filter the acceptData array based on the searchKeyword
  const filteredData = alldata.filter((data) => {
    const { strAccountType, strAccountNumber, strFirstName } = data;
    const lowerCasedSearchKeyword = searchKeyword.toLowerCase();

    return (
      (strAccountType &&
        strAccountType.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (strAccountNumber &&
        strAccountNumber.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (strFirstName &&
        strFirstName.toLowerCase().includes(lowerCasedSearchKeyword))
    );
  });
  const highlightKeyword = (text, keyword) => {
    if (!keyword || typeof text !== "string") {
      return text; // No keyword to highlight or invalid text
    }

    const regex = new RegExp(`(${escapeRegExp(keyword)})`, "gi");
    const highlightedText = text.replace(
      regex,
      '<span className="bg-yellow-200">$1</span>'
    );
    return <div dangerouslySetInnerHTML={{ __html: highlightedText }} />;
  };
  const escapeRegExp = (string) => {
    return string.replace(/[.*+?^${}()|[\]\\]/g, "\\$&"); // Escape special characters
  };

  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");

  const handleShowSuccess = (resMessage) => {
    setAlertTitle("Alert");
    setAlertMessage(resMessage);
    setAlertType("blue");
    setShowAlert(true);
  };

  const handleShowError = (resMessage) => {
    setAlertTitle("Error");
    setAlertMessage(resMessage);
    setAlertType("red");
    setShowAlert(true);
  };

  const handleCloseAlert = () => {
    setShowAlert(false);
  };

  useEffect(() => {
    if (searchBy !== "") {
      tabledata();
    }
  }, [searchBy]);
  const tabledata = async () => {
    setAlldata([]);
    try {
      const response = await amsApi.post(`account/getIssuedAcount`, {
        strIsLinkedwithCard: searchBy,
      });
      if (response.data.code === "S0000") {
        setAlldata(response.data.accountInfoList);
        setCheckpoint(1);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2">
          <div className="px-4 sm:px-10 bg-blue-200 ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                View Issued Account
              </p>
            </div>
          </div>
        </div>
        <div className=" md:col-span-2 lg:col-span-1  ">
          <form className="">
            <div className="overflow-hidden shadow ">
              <div className=" px-4 sm:p-2 bg-white  ">
                <div className="grid gap-4 px-8 md:grid-cols-4 ">
                  <div>
                    <label
                      htmlFor="text"
                      className="block  text-xs font-medium text-gray-900 dark:text-white"
                    >
                      Search By
                    </label>
                    <select
                      id="accountType"
                      name="accountType"
                      value={searchBy}
                      onChange={(e) => setsearchBy(e.target.value)}
                      className="bg-gray-50 border border-gray-300 text-gray-900 text-xs rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-1.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                      required
                    >
                      <option value="">Select </option>
                      <option value="y">Account Linked With Card</option>
                      <option value="n">Account Not Linked With Card</option>
                    </select>
                  </div>
                </div>
              </div>
            </div>
          </form>
        </div>
        <div className="flex justify-end items-center sm:px-6 lg:px-8 bg-blue-100 rounded-t-lg mt-1 ">
          <div className=" flex justify-center  rounded-md border border-transparent  px-5 mx-4 text-xs  font-medium text-white ">
            <div className="pt-2 relative  mx-auto text-gray-600">
              <input
                title="Search Here"
                type="text"
                value={searchKeyword}
                onChange={handleSearch}
                className="block w-full px-3 py-1 text-base font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                placeholder="Search.."
                style={{
                  backgroundColor: checkpoint === 1 ? "white" : "lightgray",
                  // You can set other styles here as needed
                }}
                disabled={checkpoint !== 1}
              />
              {""}
              <button
                type="submit"
                className="absolute right-0 top-0 mt-5 mr-4 px-4  "
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  strokeWidth={1.5}
                  stroke="currentColor"
                  className="w-6 h-6"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z"
                  />
                </svg>
              </button>
            </div>
          </div>
        </div>

        {/* table */}
        <div className="overflow-x-auto relative shadow-md ">
          <div className="table-wrp block max-h-[30rem] ">
            <div className="overflow-x-auto relative shadow-md  ">
              <table className="w-full text-xs text-left text-clack dark:text-blue-100 font-normal">
                <thead className="text-xs border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                  <tr>
                    <th scope="col" className="py-2.5 px-6">
                      ACCOUNT TYPE
                    </th>
                    <th scope="col" className="py-2.5 px-6">
                      ACCOUNT NUMBER
                    </th>
                    <th scope="col" className="py-2.5 px-6">
                      FIRST NAME
                    </th>
                    <th scope="col" className="py-2.5 px-6">
                      Edit
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {filteredData.length > 0 ? (
                    <>
                      {filteredData.map(
                        ({
                          strAccountType,
                          strAccountNumber,
                          strFirstName,
                        }) => (
                          <tr className="bg-blue-00 border-b  hover:bg-blue-200 border-blue-400">
                            <td className="px-6 py-2.5 whitespace-nowrap">
                              <span className="text-xs font-medium text-gray-900">
                                {highlightKeyword(
                                  strAccountType || "-",
                                  searchKeyword
                                )}
                              </span>
                            </td>
                            <td className="px-6 py-2.5 whitespace-nowrap">
                              <span className="text-xs font-medium text-gray-900">
                                {highlightKeyword(
                                  strAccountNumber || "-",
                                  searchKeyword
                                )}
                              </span>
                            </td>
                            <td className="px-6 py-2.5 whitespace-nowrap">
                              <span className="text-xs font-medium text-gray-900">
                                {highlightKeyword(
                                  strFirstName || "-",
                                  searchKeyword
                                )}
                              </span>
                            </td>
                            <td className="px-6 py-2.5 whitespace-nowrap">
                              <div className="text-xsclassName text-gray-900">
                                <NavLink
                                  title="Click Here to link"
                                  to={`/edit-account/${strAccountType}/${strAccountNumber}`}
                                >
                                  <svg
                                    xmlns="http://www.w3.org/2000/svg"
                                    fill="none"
                                    viewBox="0 0 24 24"
                                    strokeWidth={1.5}
                                    stroke="blue"
                                    className="w-6 h-6"
                                  >
                                    <path
                                      strokeLinecap="round"
                                      strokeLinejoin="round"
                                      d="M16.862 4.487l1.687-1.688a1.875 1.875 0 112.652 2.652L10.582 16.07a4.5 4.5 0 01-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 011.13-1.897l8.932-8.931zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0115.75 21H5.25A2.25 2.25 0 013 18.75V8.25A2.25 2.25 0 015.25 6H10"
                                    />
                                  </svg>
                                </NavLink>
                              </div>
                            </td>
                          </tr>
                        )
                      )}
                    </>
                  ) : (
                    <td colSpan="12" className="px-6 py-1.5 text-center">
                      <span className="text-sm font-normal text-gray-500">
                        No data available.
                      </span>
                    </td>
                  )}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
      {/* Your existing code here */}
      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
    </AppLayout>
  );
}
