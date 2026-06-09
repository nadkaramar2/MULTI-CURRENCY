import React, { useEffect, useState } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import { Link } from "react-router-dom";
import CustomAlert from "../../layout/CustomAlert";
export default function ViewEditCustomer() {
  const [alldata, setAlldata] = useState([]);
  const [checkpoint, setCheckpoint] = useState(0);
  const [searchKeyword, setSearchKeyword] = useState("");

  //   Pagination

  let [tcount, setTcount] = useState(0);
  // Function to handle changes in the search input field
  const handleSearch = (event) => {
    setSearchKeyword(event.target.value);
  };
  // Filter the acceptData array based on the searchKeyword
  const filteredData = alldata.filter((data) => {
    const { strCustId, strFirstName, strMobileNo } = data;
    const lowerCasedSearchKeyword = searchKeyword.toLowerCase();
    return (
      (strCustId &&
        strCustId.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (strFirstName &&
        strFirstName.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (strMobileNo &&
        strMobileNo.toLowerCase().includes(lowerCasedSearchKeyword))
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
    editCustomer();
  }, []);
  const editCustomer = async () => {
    try {
      const response = await amsApi.post(
        `customerId/getCustmerAccountInfo`,
        {}
      );
      if (response.data.code === "S0000") {
        setAlldata(response.data.customerIdCreationlist);
        setTcount(response.data[0].tcount);
        setCheckpoint(1);
      } else {
        // handleShowError(response.data.message);
      }
    } catch (error) {
      // handleShowError(error.response.data.message);
    }
  };

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-200 ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                View Or Edit Customer Account
              </p>
            </div>
          </div>
        </div>
        {/* <div className="flex justify-end items-center sm:px-6 lg:px-8  rounded-t-l ">
          <div className=" flex justify-center  rounded-md border border-transparent  px-5 mx-4 text-sm  font-medium text-white ">
            <div className="pt-2 relative  mx-auto text-gray-600">
              <input
                title="Search Data"
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
        </div> */}

        {/* <AutoPagintation
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={filteredData[0]?.strTotalCount}
          reCallApi={CardDetails}
        /> */}
        {/* table */}
        <div className="bg-white overflow-x-auto relative shadow-md  ">
          <div className="table-wrp block max-h-[27rem] ">
            <table className="w-full text-xs text-left text-clack dark:text-blue-100">
              <thead className="text-xs border-b sticky top-0 text-gray-500 uppercase bg-gray-100 dark:text-white">
                <tr>
                  <th scope="col" className="py-2 px-6">
                    CUSTOMER ID
                  </th>
                  <th scope="col" className="py-2 px-6">
                    FIRST NAME
                  </th>
                  <th scope="col" className="py-2 px-6">
                    MOBILE NUMBER
                  </th>
                  <th scope="col" className="py-2 px-6">
                    EDIT
                  </th>
                </tr>
              </thead>
              <tbody>
                {filteredData.length > 0 ? (
                  <>
                    {filteredData.map(
                      ({ strCustId, strFirstName, strMobileNo }) => (
                        <tr
                          key={uuidv4()}
                          className="bg-blue-00 border-b  hover:bg-blue-200 border-blue-400"
                        >
                          <td className="px-6 py-2 whitespace-nowrap">
                            <span className="text-sm font-medium text-gray-900">
                              {highlightKeyword(
                                strCustId || "-",
                                searchKeyword
                              )}
                            </span>
                          </td>
                          <td className="px-6 py-2 whitespace-nowrap">
                            <span className="text-sm font-medium text-gray-900">
                              {highlightKeyword(
                                strFirstName || "-",
                                searchKeyword
                              )}
                            </span>
                          </td>
                          <td className="px-6 py-2 whitespace-nowrap">
                            <span className="text-sm font-medium text-gray-900">
                              {highlightKeyword(
                                strMobileNo || "-",
                                searchKeyword
                              )}
                            </span>
                          </td>
                          <td className="px-6 py-2 whitespace-nowrap">
                            <div className="text-smclassName text-gray-900">
                              <Link to={`/edit-customer/${strCustId}`}>
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
                              </Link>
                            </div>
                          </td>
                        </tr>
                      )
                    )}
                  </>
                ) : (
                  <td colSpan="12" className="px-6 py-2 text-center">
                    <span className="text-lg font-medium text-gray-500">
                      No data available.
                    </span>
                  </td>
                )}
              </tbody>
            </table>
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
      {/* </div> */}
    </AppLayout>
  );
}
